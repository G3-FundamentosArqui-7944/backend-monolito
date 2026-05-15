package pe.edu.upc.bodymatch.matchmaking.domain.model.queries;

import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

public record GetRecommendedCoachesQuery(UserId athleteId, int limit) {
}
