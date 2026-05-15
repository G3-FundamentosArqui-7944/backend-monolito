package pe.edu.upc.bodymatch.videos.interfaces.rest.transform;

import pe.edu.upc.bodymatch.videos.domain.model.entities.TechnicalFeedback;
import pe.edu.upc.bodymatch.videos.interfaces.rest.resources.TechnicalFeedbackResource;

public class TechnicalFeedbackResourceFromEntityAssembler {
    public static TechnicalFeedbackResource toResourceFromEntity(TechnicalFeedback feedback) {
        return new TechnicalFeedbackResource(
                feedback.getId(),
                feedback.getAspect(),
                feedback.getMessage(),
                feedback.getSeverity().name(),
                feedback.getTimestampSeconds());
    }
}
