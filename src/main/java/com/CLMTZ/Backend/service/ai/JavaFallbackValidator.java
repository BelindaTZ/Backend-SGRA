package com.CLMTZ.Backend.service.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.CLMTZ.Backend.dto.ai.AIValidationIssue;
import com.CLMTZ.Backend.dto.ai.AIValidationResult;

import lombok.extern.slf4j.Slf4j;

/**
 * Validador de respaldo basado en reglas Java puras.
 * Se usa cuando la API de Groq no esta disponible, falla, o excede el timeout.
 *
 * Implementa validaciones basicas:
 * - Campos obligatorios vacios/nulos
 * - Formatos de cedula ecuatoriana (10 digitos)
 * - Formatos de correo electronico (regex)
 * - Duplicados exactos dentro del mismo Excel
 * - Rangos de valores (dia de semana 1-7, horas validas, etc.)
 *
 * TODO: Implementar cada metodo de validacion cuando se active la funcionalidad.
 */
@Slf4j
@Service
public class JavaFallbackValidator {

    /**
     * Ejecuta todas las validaciones de fallback sobre las filas del Excel.
     *
     * @param rows           filas del Excel como mapas clave-valor
     * @param loadType       tipo de carga ("students", "teachers", "class_schedules", etc.)
     * @param requiredFields campos obligatorios para este tipo de carga
     * @return resultado de validacion con los issues encontrados
     */
    public AIValidationResult validate(List<Map<String, Object>> rows, String loadType, List<String> requiredFields) {
        log.info("[JavaFallbackValidator] Ejecutando validacion fallback para tipo: {} con {} filas", loadType, rows.size());
        long startTime = System.currentTimeMillis();

        List<AIValidationIssue> issues = new ArrayList<>();

        // TODO: Implementar validaciones reales
        // 1. validateRequiredFields(rows, requiredFields, issues);
        // 2. validateFormats(rows, loadType, issues);
        // 3. validateDuplicates(rows, loadType, issues);
        // 4. validateRanges(rows, loadType, issues);

        long elapsed = System.currentTimeMillis() - startTime;

        String action = issues.stream().anyMatch(i -> i.getSeverity() == AIValidationIssue.Severity.ERROR)
                ? "REVIEW" : "PROCEED";

        return AIValidationResult.builder()
                .issues(issues)
                .aiValidated(false)
                .recommendedAction(action)
                .summary("Validacion por reglas Java (fallback). " + issues.size() + " problemas encontrados.")
                .validationTimeMs(elapsed)
                .build();
    }

    // -- Metodos stub para implementar despues --

    /**
     * Valida que los campos obligatorios no esten vacios o nulos.
     * TODO: Implementar
     */
    protected void validateRequiredFields(List<Map<String, Object>> rows, List<String> requiredFields,
                                          List<AIValidationIssue> issues) {
        // TODO: Recorrer cada fila, verificar que requiredFields no sean null/empty
        // Agregar AIValidationIssue con severity=ERROR, source="FALLBACK"
    }

    /**
     * Valida formatos especificos segun el tipo de carga (cedula, correo, etc.).
     * TODO: Implementar
     */
    protected void validateFormats(List<Map<String, Object>> rows, String loadType,
                                   List<AIValidationIssue> issues) {
        // TODO: Segun loadType, aplicar regex:
        // - Cedula: ^\d{10}$
        // - Correo: ^[\w.+-]+@[\w-]+\.[\w.]+$
        // - Genero: ^[MF]$
    }

    /**
     * Detecta filas duplicadas exactas dentro del mismo Excel.
     * TODO: Implementar
     */
    protected void validateDuplicates(List<Map<String, Object>> rows, String loadType,
                                      List<AIValidationIssue> issues) {
        // TODO: Identificar duplicados por campo clave (cedula para students, etc.)
        // Agregar AIValidationIssue con severity=WARNING, source="FALLBACK"
    }

    /**
     * Valida que los valores numericos esten en rangos validos.
     * TODO: Implementar
     */
    protected void validateRanges(List<Map<String, Object>> rows, String loadType,
                                  List<AIValidationIssue> issues) {
        // TODO: Para class_schedules: diaSemana entre 1-7, horas validas, etc.
    }
}
