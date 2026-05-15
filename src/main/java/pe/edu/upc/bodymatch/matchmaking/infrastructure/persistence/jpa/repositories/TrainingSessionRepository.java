package pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.TrainingSession;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

import java.time.Instant;
import java.util.List;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    List<TrainingSession> findAllByAthleteIdOrderByScheduledAtDesc(UserId athleteId);
    List<TrainingSession> findAllByCoachIdOrderByScheduledAtDesc(UserId coachId);
    List<TrainingSession> findAllByCoachIdAndScheduledAtBetween(UserId coachId, Instant start, Instant end);
}
