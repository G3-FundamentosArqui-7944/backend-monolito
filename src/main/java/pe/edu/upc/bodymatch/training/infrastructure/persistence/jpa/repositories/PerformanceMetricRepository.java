package pe.edu.upc.bodymatch.training.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.PerformanceMetric;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.MetricType;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;

import java.util.List;

@Repository
public interface PerformanceMetricRepository extends JpaRepository<PerformanceMetric, Long> {
    List<PerformanceMetric> findAllByUserIdOrderByRecordedAtDesc(UserId userId);
    List<PerformanceMetric> findAllByUserIdAndMetricTypeOrderByRecordedAtDesc(UserId userId, MetricType type);
}
