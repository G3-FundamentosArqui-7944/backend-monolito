package pe.edu.upc.bodymatch.shared.infrastructure.ai.gemini;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Base64;

@Component
public class GeminiHttpClient implements GeminiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeminiHttpClient.class);

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.base-url:https://generativelanguage.googleapis.com/v1beta}")
    private String baseUrl;

    @Value("${gemini.timeout-seconds:60}")
    private int timeoutSeconds;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private RestClient restClient() {
        var requestFactory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) Duration.ofSeconds(timeoutSeconds).toMillis());
        requestFactory.setReadTimeout((int) Duration.ofSeconds(timeoutSeconds).toMillis());
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    public String generateText(String model, String prompt) {
        if (isStubMode()) return stubResponse(prompt);
        ObjectNode payload = buildTextPayload(prompt);
        return callModel(model, payload);
    }

    @Override
    public String generateFromMultimodal(String model, String prompt, String mimeType, byte[] inlineData) {
        if (isStubMode()) return stubResponse(prompt);
        ObjectNode payload = buildMultimodalPayload(prompt, mimeType, inlineData);
        return callModel(model, payload);
    }

    private String callModel(String model, ObjectNode payload) {
        try {
            String response = restClient().post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/models/{model}:generateContent")
                            .queryParam("key", apiKey)
                            .build(model))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload.toString())
                    .retrieve()
                    .body(String.class);
            return extractText(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("Gemini call failed: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new IllegalStateException("Gemini API call failed: " + e.getStatusCode(), e);
        }
    }

    private ObjectNode buildTextPayload(String prompt) {
        ObjectNode root = objectMapper.createObjectNode();
        ArrayNode contents = root.putArray("contents");
        ObjectNode contentItem = contents.addObject();
        ArrayNode parts = contentItem.putArray("parts");
        parts.addObject().put("text", prompt);
        return root;
    }

    private ObjectNode buildMultimodalPayload(String prompt, String mimeType, byte[] inlineData) {
        ObjectNode root = objectMapper.createObjectNode();
        ArrayNode contents = root.putArray("contents");
        ObjectNode contentItem = contents.addObject();
        ArrayNode parts = contentItem.putArray("parts");
        parts.addObject().put("text", prompt);
        ObjectNode dataPart = parts.addObject();
        ObjectNode inline = dataPart.putObject("inline_data");
        inline.put("mime_type", mimeType);
        inline.put("data", Base64.getEncoder().encodeToString(inlineData));
        return root;
    }

    private String extractText(String responseBody) {
        if (responseBody == null) return "";
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode parts = candidates.get(0).path("content").path("parts");
                StringBuilder sb = new StringBuilder();
                for (JsonNode part : parts) {
                    sb.append(part.path("text").asText(""));
                }
                return sb.toString();
            }
            return "";
        } catch (Exception e) {
            LOGGER.warn("Failed to parse Gemini response: {}", e.getMessage());
            return responseBody;
        }
    }

    private boolean isStubMode() {
        return apiKey == null || apiKey.isBlank();
    }

    /**
     * Devuelve JSON simulado (fixture) cuando no hay API key configurada.
     * Detecta el tipo de análisis solicitado a partir del prompt y entrega un
     * payload válido para el parser correspondiente. Permite probar el flujo
     * completo end-to-end sin depender de Gemini.
     */
    private String stubResponse(String prompt) {
        if (prompt == null) prompt = "";
        LOGGER.warn("Gemini API key not configured — returning stubbed analysis result");
        if (prompt.contains("detectedFoods")) {
            return """
                    {
                      "summary": "Plato balanceado con proteína magra, carbohidratos complejos y vegetales. Buen aporte calórico para el almuerzo.",
                      "detectedFoods": [
                        {
                          "foodName": "Pechuga de pollo a la plancha",
                          "portionGrams": 150,
                          "calories": 248,
                          "proteinGrams": 46.5,
                          "carbohydratesGrams": 0,
                          "fatGrams": 5.4,
                          "fiberGrams": 0,
                          "confidence": 0.92
                        },
                        {
                          "foodName": "Arroz blanco cocido",
                          "portionGrams": 180,
                          "calories": 234,
                          "proteinGrams": 4.3,
                          "carbohydratesGrams": 51.5,
                          "fatGrams": 0.4,
                          "fiberGrams": 0.6,
                          "confidence": 0.88
                        },
                        {
                          "foodName": "Ensalada mixta (lechuga, tomate, pepino)",
                          "portionGrams": 100,
                          "calories": 22,
                          "proteinGrams": 1.2,
                          "carbohydratesGrams": 4.5,
                          "fatGrams": 0.2,
                          "fiberGrams": 1.8,
                          "confidence": 0.81
                        }
                      ]
                    }
                    """;
        }
        if (prompt.contains("overallScore") || prompt.contains("feedback")) {
            return """
                    {
                      "summary": "Buena ejecución general. La profundidad es correcta y el control excéntrico está presente. Trabajar la alineación de rodillas y la activación del core para evitar compensaciones lumbares.",
                      "overallScore": 78,
                      "feedback": [
                        {
                          "aspect": "Alineación de rodillas",
                          "message": "Las rodillas tienden a colapsar hacia adentro en la fase concéntrica. Empuja activamente hacia afuera durante todo el rango.",
                          "severity": "WARNING",
                          "timestampSeconds": 4
                        },
                        {
                          "aspect": "Profundidad",
                          "message": "Excelente profundidad, alcanzas paralelo o ligeramente por debajo de forma consistente.",
                          "severity": "INFO",
                          "timestampSeconds": 2
                        },
                        {
                          "aspect": "Postura lumbar",
                          "message": "Se observa una ligera pérdida de tensión en la zona lumbar al subir. Refuerza la activación del core con respiración diafragmática (Valsalva).",
                          "severity": "WARNING",
                          "timestampSeconds": 6
                        }
                      ]
                    }
                    """;
        }
        return "{\"summary\": \"\"}";
    }
}
