package pe.edu.upc.bodymatch.matchmaking.domain.model.commands;

import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

public record RequestCoachingCommand(UserId athleteId, UserId coachId, String message) {
}
