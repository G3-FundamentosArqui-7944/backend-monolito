package pe.edu.upc.bodymatch.videos.infrastructure.ai.gemini;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.shared.infrastructure.ai.gemini.GeminiClient;
import pe.edu.upc.bodymatch.videos.application.internal.outboundservices.ai.ExerciseVideoAiAnalyzer;
import pe.edu.upc.bodymatch.videos.application.internal.outboundservices.ai.dto.ExerciseAnalysisResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeminiExerciseVideoAnalyzer implements ExerciseVideoAiAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeminiExerciseVideoAnalyzer.class);
    private static final String PROMPT_TEMPLATE = """
            You are a senior strength and conditioning coach analyzing exercise execution from a video.
            The athlete is performing: %s.

            Respond ONLY with strict JSON matching this schema (no markdown, no commentary):
            {
              "summary": "string, 1-3 sentences",
              "overallScore": number between 0 and 100,
              "feedback": [
                { "aspect": "string", "message": "string", "severity": "INFO|WARNING|CRITICAL", "timestampSeconds": integer or null }
              ]
            }

            Focus on form, range of motion, joint alignment, tempo, and safety.
            """;

    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.model.exercise:gemini-2.5-flash}")
    private String exerciseModel;

    public GeminiExerciseVideoAnalyzer(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    @Override
    public ExerciseAnalysisResult analyze(String exerciseName, String mimeType, byte[] videoContent) {
        String prompt = String.format(PROMPT_TEMPLATE, exerciseName);
        String rawResponse = geminiClient.generateFromMultimodal(exerciseModel, prompt,
                mimeType != null ? mimeType : "video/mp4", videoContent);
        return parseResponse(rawResponse);
    }

    private ExerciseAnalysisResult parseResponse(String raw) {
        try {
            String cleaned = stripCodeFences(raw);
            JsonNode root = objectMapper.readTree(cleaned);
            String summary = root.path("summary").asText("Analysis unavailable");
            BigDecimal score = readScore(root);
            List<ExerciseAnalysisResult.FeedbackItem> items = new ArrayList<>();
            JsonNode feedback = root.path("feedback");
            if (feedback.isArray()) {
                for (JsonNode node : feedback) {
                    items.add(new ExerciseAnalysisResult.FeedbackItem(
                            node.path("aspect").asText("general"),
                            node.path("message").asText(""),
                            node.path("severity").asText("INFO"),
                            node.path("timestampSeconds").isNumber()
                                    ? node.path("timestampSeconds").asInt()
                                    : null));
                }
            }
            return new ExerciseAnalysisResult(summary, score, exerciseModel, items);
        } catch (Exception e) {
            LOGGER.warn("Could not parse Gemini exercise response, returning fallback: {}", e.getMessage());
            return new ExerciseAnalysisResult(
                    "Automated analysis unavailable; raw response: " + truncate(raw),
                    BigDecimal.ZERO,
                    exerciseModel,
                    List.of());
        }
    }

    private BigDecimal readScore(JsonNode root) {
        JsonNode node = root.path("overallScore");
        if (node.isNumber()) {
            BigDecimal value = node.decimalValue();
            return value.max(BigDecimal.ZERO).min(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    private String stripCodeFences(String text) {
        if (text == null) return "{}";
        String trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            int firstNewline = trimmed.indexOf('\n');
            if (firstNewline > -1) trimmed = trimmed.substring(firstNewline + 1);
            if (trimmed.endsWith("```")) trimmed = trimmed.substring(0, trimmed.length() - 3);
        }
        return trimmed.trim();
    }

    private String truncate(String text) {
        if (text == null) return "";
        return text.length() > 500 ? text.substring(0, 500) : text;
    }
}
