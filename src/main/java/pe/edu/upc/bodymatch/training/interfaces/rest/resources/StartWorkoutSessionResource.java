package pe.edu.upc.bodymatch.training.interfaces.rest.resources;

import java.time.Instant;

public record StartWorkoutSessionResource(Long userId, String title, Instant startedAt, String notes) {
}
