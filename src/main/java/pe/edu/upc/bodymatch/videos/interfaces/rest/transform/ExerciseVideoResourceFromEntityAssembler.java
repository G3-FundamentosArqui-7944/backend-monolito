package pe.edu.upc.bodymatch.videos.interfaces.rest.transform;

import pe.edu.upc.bodymatch.videos.domain.model.aggregates.ExerciseVideo;
import pe.edu.upc.bodymatch.videos.interfaces.rest.resources.ExerciseVideoResource;

public class ExerciseVideoResourceFromEntityAssembler {
    public static ExerciseVideoResource toResourceFromEntity(ExerciseVideo video) {
        return new ExerciseVideoResource(
                video.getId(),
                video.getUserId().userId(),
                video.getExerciseName(),
                video.getDescription(),
                video.getStorageUrl(),
                video.getSizeBytes(),
                video.getContentType(),
                video.getDurationSeconds(),
                video.getStatus().name(),
                video.getFailureReason(),
                VideoAnalysisResourceFromEntityAssembler.toResourceFromEntity(video.getAnalysis()));
    }
}
