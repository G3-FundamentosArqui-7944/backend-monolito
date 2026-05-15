package pe.edu.upc.bodymatch.videos.application.internal.outboundservices.ai.dto;

import java.math.BigDecimal;
import java.util.List;

public record ExerciseAnalysisResult(
        String summary,
        BigDecimal overallScore,
        String aiModelVersion,
        List<FeedbackItem> feedback) {

    public record FeedbackItem(String aspect, String message, String severity, Integer timestampSeconds) {
    }
}
