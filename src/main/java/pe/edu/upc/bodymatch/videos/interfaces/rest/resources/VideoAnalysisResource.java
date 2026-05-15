package pe.edu.upc.bodymatch.videos.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record VideoAnalysisResource(
        Long id,
        String summary,
        BigDecimal overallScore,
        String aiModelVersion,
        Instant analyzedAt,
        List<TechnicalFeedbackResource> feedbackItems) {
}
