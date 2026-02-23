package com.CLMTZ.Backend.service.ai;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.CLMTZ.Backend.config.GroqProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Cliente HTTP para la API de Groq Cloud.
 * Envía prompts y recibe respuestas de la IA.
 *
 * TODO: Implementar llamada real a la API de Groq cuando se active la funcionalidad.
 * Por ahora solo define la estructura y contrato del servicio.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroqAIService {

    private final GroqProperties groqProperties;

    /**
     * Envía un prompt al modelo Groq y retorna la respuesta como String.
     *
     * @param systemPrompt instrucciones del sistema (rol del modelo)
     * @param userPrompt   datos del usuario a analizar
     * @return respuesta del modelo como texto plano (se espera JSON)
     * @throws GroqAIException si la API falla, timeout, o respuesta inválida
     */
    public String chat(String systemPrompt, String userPrompt) {
        // TODO: Implementar llamada HTTP real a Groq
        // 1. Construir payload JSON con model, messages[], max_tokens, temperature
        // 2. POST a groqProperties.getApi().getUrl()
        // 3. Header: Authorization: Bearer {groqProperties.getApi().getKey()}
        // 4. Parsear response.choices[0].message.content
        // 5. Manejar timeouts (groqProperties.getTimeoutSeconds()) y reintentos
        log.warn("[GroqAIService] chat() llamado pero AÚN NO IMPLEMENTADO. Retornando null para activar fallback.");
        return null;
    }

    /**
     * Verifica si el servicio de Groq está configurado y disponible.
     *
     * @return true si la API key está configurada y no está vacía
     */
    public boolean isAvailable() {
        String key = groqProperties.getApi().getKey();
        return key != null && !key.isBlank();
    }

    /**
     * Construye el payload JSON para la API de Groq.
     *
     * @param systemPrompt instrucciones del sistema
     * @param userPrompt   mensaje del usuario
     * @return mapa con la estructura del request body
     */
    protected Map<String, Object> buildRequestPayload(String systemPrompt, String userPrompt) {
        // TODO: Implementar construcción del payload
        // Estructura esperada:
        // {
        //   "model": groqProperties.getModel(),
        //   "messages": [
        //     {"role": "system", "content": systemPrompt},
        //     {"role": "user", "content": userPrompt}
        //   ],
        //   "max_tokens": groqProperties.getMaxTokens(),
        //   "temperature": groqProperties.getTemperature(),
        //   "response_format": {"type": "json_object"}
        // }
        return Map.of();
    }

    // ── Excepción personalizada ──

    /**
     * Excepción lanzada cuando la API de Groq falla.
     */
    public static class GroqAIException extends RuntimeException {
        public GroqAIException(String message) {
            super(message);
        }

        public GroqAIException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

