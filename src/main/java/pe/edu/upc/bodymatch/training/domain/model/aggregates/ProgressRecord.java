package pe.edu.upc.bodymatch.training.domain.model.aggregates;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.Instant;

@Entity
@NoArgsConstructor
public class ProgressRecord extends AuditableAbstractAggregateRoot<ProgressRecord> {

    @Embedded
    @Getter
    @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Getter
    @Column(nullable = false, length = 120)
    private String milestone;

    @Getter
    @Column(length = 1000)
    private String description;

    @Getter
    @Column(nullable = false)
    private Instant achievedAt;

    public ProgressRecord(UserId userId, String milestone, String description, Instant achievedAt) {
        if (milestone == null || milestone.isBlank()) {
            throw new IllegalArgumentException("Milestone is required");
        }
        this.userId = userId;
        this.milestone = milestone;
        this.description = description;
        this.achievedAt = achievedAt == null ? Instant.now() : achievedAt;
    }
}
