package pe.edu.upc.bodymatch.training.interfaces.rest.resources;

import java.time.Instant;

public record ProgressRecordResource(
        Long id,
        Long userId,
        String milestone,
        String description,
        Instant achievedAt) {
}
