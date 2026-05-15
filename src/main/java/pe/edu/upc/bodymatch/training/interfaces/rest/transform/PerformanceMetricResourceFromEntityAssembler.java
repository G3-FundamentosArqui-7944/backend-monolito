package pe.edu.upc.bodymatch.training.interfaces.rest.transform;

import pe.edu.upc.bodymatch.training.domain.model.aggregates.PerformanceMetric;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.PerformanceMetricResource;

public class PerformanceMetricResourceFromEntityAssembler {
    public static PerformanceMetricResource toResourceFromEntity(PerformanceMetric metric) {
        return new PerformanceMetricResource(
                metric.getId(),
                metric.getUserId().userId(),
                metric.getMetricType().name(),
                metric.getValue(),
                metric.getUnit(),
                metric.getRecordedAt());
    }
}
