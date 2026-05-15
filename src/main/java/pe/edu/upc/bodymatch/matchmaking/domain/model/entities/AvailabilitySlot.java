package pe.edu.upc.bodymatch.matchmaking.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.CoachProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.DayOfWeekSlot;
import pe.edu.upc.bodymatch.shared.domain.model.entities.AuditableModel;

@Entity
@NoArgsConstructor
public class AvailabilitySlot extends AuditableModel {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Getter
    private DayOfWeekSlot slot;

    @Getter
    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_profile_id")
    @Getter
    @Setter
    private CoachProfile coachProfile;

    public AvailabilitySlot(DayOfWeekSlot slot) {
        this.slot = slot;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
