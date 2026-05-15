package pe.edu.upc.bodymatch.training.domain.model.aggregates;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.bodymatch.training.domain.model.entities.ExerciseExecution;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.WorkoutSessionStatus;
import pe.edu.upc.bodymatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class WorkoutSession extends AuditableAbstractAggregateRoot<WorkoutSession> {

    @Embedded
    @Getter
    @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Getter
    @Column(nullable = false, length = 120)
    private String title;

    @Getter
    @Column(nullable = false)
    private Instant startedAt;

    @Getter
    private Instant completedAt;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkoutSessionStatus status;

    @Getter
    @Column(length = 1000)
    private String notes;

    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter
    private List<ExerciseExecution> exercises;

    public WorkoutSession(UserId userId, String title, Instant startedAt, String notes) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Workout title required");
        }
        if (startedAt == null) {
            throw new IllegalArgumentException("Start time required");
        }
        this.userId = userId;
        this.title = title;
        this.startedAt = startedAt;
        this.notes = notes;
        this.status = WorkoutSessionStatus.IN_PROGRESS;
        this.exercises = new ArrayList<>();
    }

    public void addExercise(ExerciseExecution exercise) {
        if (status == WorkoutSessionStatus.COMPLETED) {
            throw new IllegalStateException("Cannot add exercises to a completed workout");
        }
        exercise.setWorkoutSession(this);
        this.exercises.add(exercise);
    }

    public void complete() {
        if (status == WorkoutSessionStatus.COMPLETED) return;
        if (this.exercises.isEmpty()) {
            throw new IllegalStateException("Cannot complete a workout without exercises");
        }
        this.status = WorkoutSessionStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    public void abandon() {
        if (status == WorkoutSessionStatus.COMPLETED) {
            throw new IllegalStateException("Cannot abandon a completed workout");
        }
        this.status = WorkoutSessionStatus.ABANDONED;
        this.completedAt = Instant.now();
    }

    public int totalVolume() {
        return exercises.stream().mapToInt(ExerciseExecution::volume).sum();
    }
}
