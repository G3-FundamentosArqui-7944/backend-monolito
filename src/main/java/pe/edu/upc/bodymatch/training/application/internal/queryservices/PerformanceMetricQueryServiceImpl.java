package pe.edu.upc.bodymatch.training.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.PerformanceMetric;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.ProgressRecord;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.WorkoutSession;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetPerformanceMetricsByUserQuery;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetProgressRecordsByUserQuery;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetTrainingAnalyticsQuery;
import pe.edu.upc.bodymatch.training.domain.model.readmodel.TrainingAnalytics;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.WorkoutSessionStatus;
import pe.edu.upc.bodymatch.training.domain.services.PerformanceMetricQueryService;
import pe.edu.upc.bodymatch.training.infrastructure.persistence.jpa.repositories.PerformanceMetricRepository;
import pe.edu.upc.bodymatch.training.infrastructure.persistence.jpa.repositories.ProgressRecordRepository;
import pe.edu.upc.bodymatch.training.infrastructure.persistence.jpa.repositories.WorkoutSessionRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PerformanceMetricQueryServiceImpl implements PerformanceMetricQueryService {
    private final PerformanceMetricRepository performanceMetricRepository;
    private final ProgressRecordRepository progressRecordRepository;
    private final WorkoutSessionRepository workoutSessionRepository;

    public PerformanceMetricQueryServiceImpl(PerformanceMetricRepository performanceMetricRepository,
                                             ProgressRecordRepository progressRecordRepository,
                                             WorkoutSessionRepository workoutSessionRepository) {
        this.performanceMetricRepository = performanceMetricRepository;
        this.progressRecordRepository = progressRecordRepository;
        this.workoutSessionRepository = workoutSessionRepository;
    }

    @Override
    public List<PerformanceMetric> handle(GetPerformanceMetricsByUserQuery query) {
        if (query.type() != null) {
            return performanceMetricRepository.findAllByUserIdAndMetricTypeOrderByRecordedAtDesc(query.userId(), query.type());
        }
        return performanceMetricRepository.findAllByUserIdOrderByRecordedAtDesc(query.userId());
    }

    @Override
    public List<ProgressRecord> handle(GetProgressRecordsByUserQuery query) {
        return progressRecordRepository.findAllByUserIdOrderByAchievedAtDesc(query.userId());
    }

    @Override
    public TrainingAnalytics handle(GetTrainingAnalyticsQuery query) {
        var from = query.from() != null ? query.from() : Instant.now().minus(90, ChronoUnit.DAYS);
        var to = query.to() != null ? query.to() : Instant.now();
        var sessions = workoutSessionRepository.findAllByUserIdAndStartedAtBetween(query.userId(), from, to);
        long total = sessions.size();
        long completed = sessions.stream().filter(s -> s.getStatus() == WorkoutSessionStatus.COMPLETED).count();
        long abandoned = sessions.stream().filter(s -> s.getStatus() == WorkoutSessionStatus.ABANDONED).count();
        long executions = sessions.stream().mapToLong(s -> s.getExercises().size()).sum();
        long totalVolume = sessions.stream().mapToLong(WorkoutSession::totalVolume).sum();
        double avgVolume = total == 0 ? 0d : (double) totalVolume / total;
        double completionRate = total == 0 ? 0d : (double) completed / total;
        return new TrainingAnalytics(total, completed, abandoned, executions, totalVolume, avgVolume, completionRate);
    }
}
