package pe.edu.upc.bodymatch.training.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record RecordPerformanceMetricResource(
        Long userId,
        String metricType,
        BigDecimal value,
        String unit,
        Instant recordedAt) {
}
