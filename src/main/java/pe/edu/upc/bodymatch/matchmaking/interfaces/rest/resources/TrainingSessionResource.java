package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources;

import java.time.Instant;

public record TrainingSessionResource(
        Long id,
        Long athleteId,
        Long coachId,
        Instant scheduledAt,
        int durationMinutes,
        String location,
        String notes,
        String status,
        Instant completedAt) {
}
