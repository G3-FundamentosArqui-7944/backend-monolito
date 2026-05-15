package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources;

public record CreateConnectionRequestResource(Long athleteId, Long coachId, String message) {
}
