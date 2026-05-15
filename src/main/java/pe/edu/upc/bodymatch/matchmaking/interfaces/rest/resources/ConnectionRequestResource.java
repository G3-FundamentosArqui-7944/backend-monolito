package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources;

import java.time.Instant;

public record ConnectionRequestResource(
        Long id,
        Long athleteId,
        Long coachId,
        String message,
        String status,
        Instant respondedAt,
        String responseNote) {
}
