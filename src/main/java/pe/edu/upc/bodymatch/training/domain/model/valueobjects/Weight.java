package pe.edu.upc.bodymatch.training.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record Weight(BigDecimal value, String unit) {
    public Weight {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Weight must be non-negative");
        }
        if (unit == null || unit.isBlank()) {
            unit = "kg";
        }
    }

    public Weight() {
        this(BigDecimal.ZERO, "kg");
    }
}
