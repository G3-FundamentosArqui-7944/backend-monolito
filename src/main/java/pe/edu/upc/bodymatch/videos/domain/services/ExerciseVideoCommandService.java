package pe.edu.upc.bodymatch.videos.domain.services;

import pe.edu.upc.bodymatch.videos.domain.model.aggregates.ExerciseVideo;
import pe.edu.upc.bodymatch.videos.domain.model.commands.AnalyzeExerciseVideoCommand;
import pe.edu.upc.bodymatch.videos.domain.model.commands.DeleteExerciseVideoCommand;
import pe.edu.upc.bodymatch.videos.domain.model.commands.UploadExerciseVideoCommand;

import java.util.Optional;

public interface ExerciseVideoCommandService {
    Optional<ExerciseVideo> handle(UploadExerciseVideoCommand command);
    Optional<ExerciseVideo> handle(AnalyzeExerciseVideoCommand command);
    void handle(DeleteExerciseVideoCommand command);
}
