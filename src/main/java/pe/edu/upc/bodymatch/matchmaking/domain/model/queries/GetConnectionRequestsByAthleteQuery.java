package pe.edu.upc.bodymatch.matchmaking.domain.model.queries;

import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

public record GetConnectionRequestsByAthleteQuery(UserId athleteId) {
}
