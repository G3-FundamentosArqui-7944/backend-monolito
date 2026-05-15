package pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.ConnectionRequest;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.ConnectionRequestStatus;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Long> {
    List<ConnectionRequest> findAllByAthleteIdOrderByCreatedAtDesc(UserId athleteId);
    List<ConnectionRequest> findAllByCoachIdOrderByCreatedAtDesc(UserId coachId);
    List<ConnectionRequest> findAllByCoachIdAndStatus(UserId coachId, ConnectionRequestStatus status);
    Optional<ConnectionRequest> findByAthleteIdAndCoachIdAndStatus(UserId athleteId, UserId coachId, ConnectionRequestStatus status);
}
