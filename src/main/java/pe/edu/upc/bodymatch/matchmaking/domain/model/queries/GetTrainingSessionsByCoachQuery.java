package pe.edu.upc.bodymatch.matchmaking.domain.model.queries;

import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

public record GetTrainingSessionsByCoachQuery(UserId coachId) {
}
