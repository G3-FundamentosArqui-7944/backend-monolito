package pe.edu.upc.bodymatch.training.domain.model.aggregates;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.MetricType;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@NoArgsConstructor
public class PerformanceMetric extends AuditableAbstractAggregateRoot<PerformanceMetric> {

    @Embedded
    @Getter
    @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MetricType metricType;

    @Getter
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal value;

    @Getter
    @Column(nullable = false, length = 10)
    private String unit;

    @Getter
    @Column(nullable = false)
    private Instant recordedAt;

    public PerformanceMetric(UserId userId, MetricType metricType, BigDecimal value, String unit, Instant recordedAt) {
        if (metricType == null) throw new IllegalArgumentException("Metric type is required");
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Metric value must be non-negative");
        }
        this.userId = userId;
        this.metricType = metricType;
        this.value = value;
        this.unit = unit == null ? defaultUnitFor(metricType) : unit;
        this.recordedAt = recordedAt == null ? Instant.now() : recordedAt;
    }

    private static String defaultUnitFor(MetricType type) {
        return switch (type) {
            case BODY_WEIGHT_KG, MUSCLE_MASS_KG, ONE_REP_MAX_KG -> "kg";
            case BODY_FAT_PERCENT -> "%";
            case RESTING_HEART_RATE -> "bpm";
            case VO2_MAX -> "ml/kg/min";
            case WAIST_CM, CHEST_CM, BICEP_CM, THIGH_CM, HEIGHT_CM -> "cm";
        };
    }
}
