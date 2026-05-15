package pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.CoachProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachProfileRepository extends JpaRepository<CoachProfile, Long> {
    Optional<CoachProfile> findByUserId(UserId userId);
    boolean existsByUserId(UserId userId);
    List<CoachProfile> findAllByAcceptingClientsTrue();
}
