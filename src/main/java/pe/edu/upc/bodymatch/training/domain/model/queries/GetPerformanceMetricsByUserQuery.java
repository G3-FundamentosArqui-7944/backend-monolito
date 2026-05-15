package pe.edu.upc.bodymatch.training.domain.model.queries;

import pe.edu.upc.bodymatch.training.domain.model.valueobjects.MetricType;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;

public record GetPerformanceMetricsByUserQuery(UserId userId, MetricType type) {
}
