package pe.edu.upc.bodymatch.training.interfaces.rest.resources;

import java.math.BigDecimal;

public record ExerciseExecutionResource(
        Long id,
        String exerciseName,
        int sets,
        int reps,
        BigDecimal load,
        String loadUnit,
        Integer durationSeconds,
        Integer restSeconds,
        String notes) {
}
