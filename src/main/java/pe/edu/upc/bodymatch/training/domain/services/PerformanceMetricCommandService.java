package pe.edu.upc.bodymatch.training.domain.services;

import pe.edu.upc.bodymatch.training.domain.model.aggregates.PerformanceMetric;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.ProgressRecord;
import pe.edu.upc.bodymatch.training.domain.model.commands.RecordPerformanceMetricCommand;
import pe.edu.upc.bodymatch.training.domain.model.commands.RegisterProgressMilestoneCommand;

import java.util.Optional;

public interface PerformanceMetricCommandService {
    Optional<PerformanceMetric> handle(RecordPerformanceMetricCommand command);
    Optional<ProgressRecord> handle(RegisterProgressMilestoneCommand command);
}
