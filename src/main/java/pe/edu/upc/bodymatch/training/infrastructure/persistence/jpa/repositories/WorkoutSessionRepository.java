package pe.edu.upc.bodymatch.training.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.WorkoutSession;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;

import java.time.Instant;
import java.util.List;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
    List<WorkoutSession> findAllByUserIdOrderByStartedAtDesc(UserId userId);
    List<WorkoutSession> findAllByUserIdAndStartedAtBetween(UserId userId, Instant from, Instant to);
}
