package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources;

import java.time.Instant;

public record ScheduleTrainingSessionResource(
        Long athleteId,
        Long coachId,
        Instant scheduledAt,
        int durationMinutes,
        String location,
        String notes) {
}
