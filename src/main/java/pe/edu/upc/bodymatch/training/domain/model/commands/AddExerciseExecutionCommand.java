package pe.edu.upc.bodymatch.training.domain.model.commands;

import java.math.BigDecimal;

public record AddExerciseExecutionCommand(
        Long workoutSessionId,
        String exerciseName,
        int sets,
        int reps,
        BigDecimal load,
        String loadUnit,
        Integer durationSeconds,
        Integer restSeconds,
        String notes) {
}
