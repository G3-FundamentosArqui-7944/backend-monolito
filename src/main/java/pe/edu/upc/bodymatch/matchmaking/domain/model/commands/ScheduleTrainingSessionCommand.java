package pe.edu.upc.bodymatch.matchmaking.domain.model.commands;

import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

import java.time.Instant;

public record ScheduleTrainingSessionCommand(
        UserId athleteId,
        UserId coachId,
        Instant scheduledAt,
        int durationMinutes,
        String location,
        String notes) {
}
