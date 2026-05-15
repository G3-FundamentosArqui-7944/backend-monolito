package pe.edu.upc.bodymatch.training.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.training.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.PerformanceMetric;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.ProgressRecord;
import pe.edu.upc.bodymatch.training.domain.model.commands.RecordPerformanceMetricCommand;
import pe.edu.upc.bodymatch.training.domain.model.commands.RegisterProgressMilestoneCommand;
import pe.edu.upc.bodymatch.training.domain.services.PerformanceMetricCommandService;
import pe.edu.upc.bodymatch.training.infrastructure.persistence.jpa.repositories.PerformanceMetricRepository;
import pe.edu.upc.bodymatch.training.infrastructure.persistence.jpa.repositories.ProgressRecordRepository;

import java.util.Optional;

@Service
public class PerformanceMetricCommandServiceImpl implements PerformanceMetricCommandService {
    private final PerformanceMetricRepository performanceMetricRepository;
    private final ProgressRecordRepository progressRecordRepository;
    private final ExternalIamService externalIamService;

    public PerformanceMetricCommandServiceImpl(PerformanceMetricRepository performanceMetricRepository,
                                               ProgressRecordRepository progressRecordRepository,
                                               ExternalIamService externalIamService) {
        this.performanceMetricRepository = performanceMetricRepository;
        this.progressRecordRepository = progressRecordRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public Optional<PerformanceMetric> handle(RecordPerformanceMetricCommand command) {
        if (!externalIamService.existsUser(command.userId())) {
            throw new IllegalArgumentException("User does not exist: " + command.userId().userId());
        }
        var metric = new PerformanceMetric(command.userId(), command.metricType(),
                command.value(), command.unit(), command.recordedAt());
        performanceMetricRepository.save(metric);
        return Optional.of(metric);
    }

    @Override
    @Transactional
    public Optional<ProgressRecord> handle(RegisterProgressMilestoneCommand command) {
        if (!externalIamService.existsUser(command.userId())) {
            throw new IllegalArgumentException("User does not exist: " + command.userId().userId());
        }
        var record = new ProgressRecord(command.userId(), command.milestone(),
                command.description(), command.achievedAt());
        progressRecordRepository.save(record);
        return Optional.of(record);
    }
}
