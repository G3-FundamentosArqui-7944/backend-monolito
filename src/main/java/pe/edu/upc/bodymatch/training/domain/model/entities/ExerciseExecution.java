package pe.edu.upc.bodymatch.training.domain.model.entities;

import jakarta.persistence.AttributeOverride;
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
import pe.edu.upc.bodymatch.training.domain.model.aggregates.WorkoutSession;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.Weight;
import pe.edu.upc.bodymatch.shared.domain.model.entities.AuditableModel;

@Entity
@NoArgsConstructor
public class ExerciseExecution extends AuditableModel {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false, length = 120)
    private String exerciseName;

    @Getter
    @Column(nullable = false)
    private int sets;

    @Getter
    @Column(nullable = false)
    private int reps;

    @Embedded
    @Getter
    @AttributeOverride(name = "value", column = @Column(name = "load_value"))
    @AttributeOverride(name = "unit", column = @Column(name = "load_unit", length = 10))
    private Weight load;

    @Getter
    @Column
    private Integer durationSeconds;

    @Getter
    @Column
    private Integer restSeconds;

    @Getter
    @Column(length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_session_id")
    @Getter
    @Setter
    private WorkoutSession workoutSession;

    public ExerciseExecution(String exerciseName, int sets, int reps, Weight load,
                             Integer durationSeconds, Integer restSeconds, String notes) {
        if (exerciseName == null || exerciseName.isBlank()) {
            throw new IllegalArgumentException("Exercise name is required");
        }
        if (sets <= 0 || reps < 0) {
            throw new IllegalArgumentException("Sets must be positive and reps non-negative");
        }
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.load = load == null ? new Weight() : load;
        this.durationSeconds = durationSeconds;
        this.restSeconds = restSeconds;
        this.notes = notes;
    }

    public int volume() {
        if (load == null || load.value() == null) return 0;
        return load.value().multiply(java.math.BigDecimal.valueOf((long) sets * reps)).intValue();
    }
}
