package pe.edu.upc.bodymatch.training.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetTrainingAnalyticsQuery;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.training.domain.services.PerformanceMetricQueryService;

@Service
public class TrainingContextFacade {
    private final PerformanceMetricQueryService performanceMetricQueryService;

    public TrainingContextFacade(PerformanceMetricQueryService performanceMetricQueryService) {
        this.performanceMetricQueryService = performanceMetricQueryService;
    }

    public long totalWorkoutsForUser(Long userId) {
        var analytics = performanceMetricQueryService.handle(new GetTrainingAnalyticsQuery(new UserId(userId), null, null));
        return analytics.totalWorkouts();
    }

    public double completionRateForUser(Long userId) {
        var analytics = performanceMetricQueryService.handle(new GetTrainingAnalyticsQuery(new UserId(userId), null, null));
        return analytics.completionRate();
    }
}
