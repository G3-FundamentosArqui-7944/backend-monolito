package pe.edu.upc.bodymatch.training.domain.services;

import pe.edu.upc.bodymatch.training.domain.model.aggregates.WorkoutSession;
import pe.edu.upc.bodymatch.training.domain.model.commands.AddExerciseExecutionCommand;
import pe.edu.upc.bodymatch.training.domain.model.commands.CompleteWorkoutSessionCommand;
import pe.edu.upc.bodymatch.training.domain.model.commands.StartWorkoutSessionCommand;

import java.util.Optional;

public interface WorkoutSessionCommandService {
    Optional<WorkoutSession> handle(StartWorkoutSessionCommand command);
    Optional<WorkoutSession> handle(AddExerciseExecutionCommand command);
    Optional<WorkoutSession> handle(CompleteWorkoutSessionCommand command);
}
