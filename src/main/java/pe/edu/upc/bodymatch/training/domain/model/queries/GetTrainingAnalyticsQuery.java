package pe.edu.upc.bodymatch.training.domain.model.queries;

import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;

import java.time.Instant;

public record GetTrainingAnalyticsQuery(UserId userId, Instant from, Instant to) {
}
