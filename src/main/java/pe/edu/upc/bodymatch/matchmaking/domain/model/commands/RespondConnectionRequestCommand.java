package pe.edu.upc.bodymatch.matchmaking.domain.model.commands;

public record RespondConnectionRequestCommand(Long requestId, boolean approve, String responseNote) {
}
