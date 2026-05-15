package pe.edu.upc.bodymatch.videos.interfaces.rest.transform;

import pe.edu.upc.bodymatch.videos.domain.model.entities.VideoAnalysis;
import pe.edu.upc.bodymatch.videos.interfaces.rest.resources.VideoAnalysisResource;

public class VideoAnalysisResourceFromEntityAssembler {
    public static VideoAnalysisResource toResourceFromEntity(VideoAnalysis analysis) {
        if (analysis == null) return null;
        var feedback = analysis.getFeedbackItems().stream()
                .map(TechnicalFeedbackResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new VideoAnalysisResource(
                analysis.getId(),
                analysis.getSummary(),
                analysis.getOverallScore(),
                analysis.getAiModelVersion(),
                analysis.getAnalyzedAt(),
                feedback);
    }
}
