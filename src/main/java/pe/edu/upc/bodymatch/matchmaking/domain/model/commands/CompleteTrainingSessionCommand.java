package pe.edu.upc.bodymatch.matchmaking.domain.model.commands;

public record CompleteTrainingSessionCommand(Long sessionId, String coachNotes) {
}
