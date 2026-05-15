package pe.edu.upc.bodymatch.videos.domain.model.commands;

import pe.edu.upc.bodymatch.videos.domain.model.valueobjects.UserId;

public record UploadExerciseVideoCommand(
        UserId userId,
        String exerciseName,
        String description,
        String originalFilename,
        String contentType,
        long sizeBytes,
        Integer durationSeconds,
        byte[] content) {
}
