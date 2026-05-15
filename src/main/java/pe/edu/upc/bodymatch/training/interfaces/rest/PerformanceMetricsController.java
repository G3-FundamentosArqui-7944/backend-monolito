package pe.edu.upc.bodymatch.training.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.training.domain.model.commands.RecordPerformanceMetricCommand;
import pe.edu.upc.bodymatch.training.domain.model.commands.RegisterProgressMilestoneCommand;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetPerformanceMetricsByUserQuery;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetProgressRecordsByUserQuery;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetTrainingAnalyticsQuery;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.MetricType;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.training.domain.services.PerformanceMetricCommandService;
import pe.edu.upc.bodymatch.training.domain.services.PerformanceMetricQueryService;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.PerformanceMetricResource;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.ProgressRecordResource;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.RecordPerformanceMetricResource;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.RegisterProgressMilestoneResource;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.TrainingAnalyticsResource;
import pe.edu.upc.bodymatch.training.interfaces.rest.transform.PerformanceMetricResourceFromEntityAssembler;
import pe.edu.upc.bodymatch.training.interfaces.rest.transform.ProgressRecordResourceFromEntityAssembler;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/training-metrics", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Training Metrics", description = "Performance metrics, progress records and analytics")
public class PerformanceMetricsController {
    private final PerformanceMetricCommandService performanceMetricCommandService;
    private final PerformanceMetricQueryService performanceMetricQueryService;

    public PerformanceMetricsController(PerformanceMetricCommandService performanceMetricCommandService,
                                        PerformanceMetricQueryService performanceMetricQueryService) {
        this.performanceMetricCommandService = performanceMetricCommandService;
        this.performanceMetricQueryService = performanceMetricQueryService;
    }

    @PostMapping("/metrics")
    public ResponseEntity<PerformanceMetricResource> recordMetric(@RequestBody RecordPerformanceMetricResource resource) {
        try {
            var command = new RecordPerformanceMetricCommand(
                    new UserId(resource.userId()),
                    MetricType.valueOf(resource.metricType()),
                    resource.value(),
                    resource.unit(),
                    resource.recordedAt());
            var metric = performanceMetricCommandService.handle(command);
            if (metric.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    PerformanceMetricResourceFromEntityAssembler.toResourceFromEntity(metric.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/metrics/user/{userId}")
    public ResponseEntity<List<PerformanceMetricResource>> metricsByUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String type) {
        MetricType metricType = type == null ? null : MetricType.valueOf(type);
        var metrics = performanceMetricQueryService.handle(new GetPerformanceMetricsByUserQuery(new UserId(userId), metricType));
        return ResponseEntity.ok(metrics.stream()
                .map(PerformanceMetricResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @PostMapping("/progress")
    public ResponseEntity<ProgressRecordResource> registerProgress(@RequestBody RegisterProgressMilestoneResource resource) {
        try {
            var command = new RegisterProgressMilestoneCommand(
                    new UserId(resource.userId()), resource.milestone(), resource.description(), resource.achievedAt());
            var record = performanceMetricCommandService.handle(command);
            if (record.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    ProgressRecordResourceFromEntityAssembler.toResourceFromEntity(record.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/progress/user/{userId}")
    public ResponseEntity<List<ProgressRecordResource>> progressByUser(@PathVariable Long userId) {
        var records = performanceMetricQueryService.handle(new GetProgressRecordsByUserQuery(new UserId(userId)));
        return ResponseEntity.ok(records.stream()
                .map(ProgressRecordResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/analytics/user/{userId}")
    public ResponseEntity<TrainingAnalyticsResource> analytics(
            @PathVariable Long userId,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to) {
        var analytics = performanceMetricQueryService.handle(new GetTrainingAnalyticsQuery(new UserId(userId), from, to));
        return ResponseEntity.ok(new TrainingAnalyticsResource(
                analytics.totalWorkouts(), analytics.completedWorkouts(), analytics.abandonedWorkouts(),
                analytics.totalExerciseExecutions(), analytics.totalVolume(),
                analytics.averageVolumePerSession(), analytics.completionRate()));
    }
}
