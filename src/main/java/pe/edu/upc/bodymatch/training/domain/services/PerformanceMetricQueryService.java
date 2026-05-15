package pe.edu.upc.bodymatch.training.domain.services;

import pe.edu.upc.bodymatch.training.domain.model.aggregates.PerformanceMetric;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.ProgressRecord;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetPerformanceMetricsByUserQuery;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetProgressRecordsByUserQuery;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetTrainingAnalyticsQuery;
import pe.edu.upc.bodymatch.training.domain.model.readmodel.TrainingAnalytics;

import java.util.List;

public interface PerformanceMetricQueryService {
    List<PerformanceMetric> handle(GetPerformanceMetricsByUserQuery query);
    List<ProgressRecord> handle(GetProgressRecordsByUserQuery query);
    TrainingAnalytics handle(GetTrainingAnalyticsQuery query);
}
