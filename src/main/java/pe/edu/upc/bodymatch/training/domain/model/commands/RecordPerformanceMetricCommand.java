package pe.edu.upc.bodymatch.training.domain.model.commands;

import pe.edu.upc.bodymatch.training.domain.model.valueobjects.MetricType;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;

import java.math.BigDecimal;
import java.time.Instant;

public record RecordPerformanceMetricCommand(
        UserId userId,
        MetricType metricType,
        BigDecimal value,
        String unit,
        Instant recordedAt) {
}
