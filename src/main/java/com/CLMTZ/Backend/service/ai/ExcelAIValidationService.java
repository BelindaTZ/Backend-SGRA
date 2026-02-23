package com.CLMTZ.Backend.service.ai;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.CLMTZ.Backend.dto.ai.AIValidationRequest;
import com.CLMTZ.Backend.dto.ai.AIValidationResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Orquestador principal de validación inteligente de datos Excel.
 *
 * Flujo:
 * 1. Recibe las filas parseadas del Excel + tipo de carga
 * 2. Construye el prompt usando la plantilla correspondiente
 * 3. Intenta validar con Groq AI (principal)
 * 4. Si Groq falla/timeout → ejecuta JavaFallbackValidator (respaldo)
 * 5. Retorna AIValidationResult con todos los issues encontrados
 *
 * TODO: Implementar la lógica real de construcción de prompts y parseo de respuesta IA.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelAIValidationService {

    private final GroqAIService groqAIService;
    private final JavaFallbackValidator fallbackValidator;

    /**
     * Valida los datos del Excel usando IA (Groq) con fallback a reglas Java.
     *
     * @param request datos a validar con su tipo de carga y reglas
     * @return resultado de la validación
     */
    public AIValidationResult validate(AIValidationRequest request) {
        log.info("[ExcelAIValidationService] Iniciando validación para tipo: {} con {} filas",
                request.getLoadType(), request.getRows().size());

        // Intentar validación con IA primero
        if (groqAIService.isAvailable()) {
            try {
                AIValidationResult aiResult = validateWithAI(request);
                if (aiResult != null) {
                    return aiResult;
                }
            } catch (Exception e) {
                log.warn("[ExcelAIValidationService] Groq AI falló, usando fallback Java. Error: {}", e.getMessage());
            }
        } else {
            log.info("[ExcelAIValidationService] Groq AI no disponible (sin API key), usando fallback Java.");
        }

        // Fallback a validaciones Java
        return fallbackValidator.validate(
                request.getRows(),
                request.getLoadType(),
                getRequiredFieldsForType(request.getLoadType())
        );
    }

    /**
     * Intenta validar los datos usando la API de Groq.
     *
     * TODO: Implementar cuando se active la funcionalidad IA.
     * 1. Cargar template de prompt desde resources/prompts/{loadType}.txt
     * 2. Construir systemPrompt + userPrompt con los datos
     * 3. Llamar a groqAIService.chat()
     * 4. Parsear respuesta JSON a AIValidationResult
     *
     * @param request datos a validar
     * @return resultado de validación IA, o null si no se pudo procesar
     */
    private AIValidationResult validateWithAI(AIValidationRequest request) {
        // TODO: Implementar llamada real a Groq AI
        // String systemPrompt = loadPromptTemplate(request.getLoadType());
        // String userPrompt = buildUserPrompt(request);
        // String aiResponse = groqAIService.chat(systemPrompt, userPrompt);
        // return parseAIResponse(aiResponse);
        log.warn("[ExcelAIValidationService] validateWithAI() AÚN NO IMPLEMENTADO.");
        return null;
    }

    /**
     * Carga la plantilla de prompt desde resources/prompts/{loadType}.txt
     *
     * @param loadType tipo de carga
     * @return contenido del prompt template
     */
    protected String loadPromptTemplate(String loadType) {
        String path = "prompts/" + loadType + ".txt";
        try {
            ClassPathResource resource = new ClassPathResource(path);
            InputStream is = resource.getInputStream();
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("[ExcelAIValidationService] No se encontró template de prompt: {}", path);
            return null;
        }
    }

    /**
     * Construye el prompt del usuario con los datos del Excel serializados.
     * TODO: Implementar serialización de filas a texto/JSON para el prompt.
     *
     * @param request datos a validar
     * @return prompt formateado con los datos
     */
    protected String buildUserPrompt(AIValidationRequest request) {
        // TODO: Serializar request.getRows() a formato que la IA pueda analizar
        // Ejemplo: convertir a tabla CSV o JSON compacto
        return "";
    }

    /**
     * Parsea la respuesta JSON del modelo IA a un AIValidationResult.
     * TODO: Implementar parseo de JSON de respuesta.
     *
     * @param aiResponse respuesta cruda del modelo
     * @return resultado estructurado
     */
    protected AIValidationResult parseAIResponse(String aiResponse) {
        // TODO: Usar ObjectMapper para parsear el JSON de la IA
        // Espera formato: { "issues": [...], "recommendedAction": "...", "summary": "..." }
        return null;
    }

    /**
     * Retorna los campos obligatorios según el tipo de carga.
     * Se usa tanto para el prompt de IA como para el fallback Java.
     *
     * @param loadType tipo de carga
     * @return lista de nombres de campos obligatorios
     */
    protected List<String> getRequiredFieldsForType(String loadType) {
        return switch (loadType) {
            case "students" -> List.of("cedula", "nombres", "apellidos", "correo", "carreraTexto", "modalidadTexto", "periodoTexto");
            case "teachers" -> List.of("cedula", "nombres", "apellidos", "correo");
            case "class_schedules" -> List.of("cedulaDocente", "nombreAsignatura", "nombreParalelo", "nombrePeriodo", "diaSemana", "horaInicio", "horaFin");
            case "careers" -> List.of("nombre", "codigo");
            case "subjects" -> List.of("nombre", "codigo");
            case "periods" -> List.of("nombre", "fechaInicio", "fechaFin");
            case "registrations" -> List.of("cedulaEstudiante", "nombreAsignatura", "nombrePeriodo");
            case "syllabi" -> List.of("nombreAsignatura", "tema", "orden");
            default -> List.of();
        };
    }

    /**
     * Retorna las reglas de negocio específicas por tipo de carga.
     * Se usan para enriquecer el prompt de IA.
     * TODO: Completar reglas específicas por cada SP.
     *
     * @param loadType tipo de carga
     * @return lista de reglas en lenguaje natural
     */
    protected List<String> getBusinessRulesForType(String loadType) {
        return switch (loadType) {
            case "students" -> List.of(
                    "La cédula debe tener exactamente 10 dígitos numéricos",
                    "El correo debe ser un email válido",
                    "El género debe ser 'M' o 'F'",
                    "No pueden existir dos estudiantes con la misma cédula en el mismo archivo"
            );
            case "class_schedules" -> List.of(
                    "El día de la semana debe estar entre 1 (Lunes) y 7 (Domingo)",
                    "La hora de inicio debe ser anterior a la hora de fin",
                    "No pueden existir horarios duplicados (mismo docente, día y hora)",
                    "La cédula del docente debe tener 10 dígitos"
            );
            default -> List.of();
        };
    }
}

