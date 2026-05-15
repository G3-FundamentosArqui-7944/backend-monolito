package pe.edu.upc.bodymatch.videos.interfaces.rest.resources;

public record TechnicalFeedbackResource(
        Long id,
        String aspect,
        String message,
        String severity,
        Integer timestampSeconds) {
}
