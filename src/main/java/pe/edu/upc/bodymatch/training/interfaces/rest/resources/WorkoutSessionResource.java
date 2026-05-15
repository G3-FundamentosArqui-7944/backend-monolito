package pe.edu.upc.bodymatch.training.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;

public record WorkoutSessionResource(
        Long id,
        Long userId,
        String title,
        Instant startedAt,
        Instant completedAt,
        String status,
        String notes,
        int totalVolume,
        List<ExerciseExecutionResource> exercises) {
}
