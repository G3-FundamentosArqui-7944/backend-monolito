package pe.edu.upc.bodymatch.videos.interfaces.rest.resources;

public record ExerciseVideoResource(
        Long id,
        Long userId,
        String exerciseName,
        String description,
        String storageUrl,
        long sizeBytes,
        String contentType,
        Integer durationSeconds,
        String status,
        String failureReason,
        VideoAnalysisResource analysis) {
}
