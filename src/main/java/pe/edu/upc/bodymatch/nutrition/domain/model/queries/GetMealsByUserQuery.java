package pe.edu.upc.bodymatch.nutrition.domain.model.queries;

import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;

import java.time.Instant;

public record GetMealsByUserQuery(UserId userId, Instant from, Instant to) {
}
