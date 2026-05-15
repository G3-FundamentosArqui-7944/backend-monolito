package pe.edu.upc.bodymatch.matchmaking.domain.model.commands;

import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record AddCoachAvailabilityCommand(
        UserId coachId,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime) {
}
