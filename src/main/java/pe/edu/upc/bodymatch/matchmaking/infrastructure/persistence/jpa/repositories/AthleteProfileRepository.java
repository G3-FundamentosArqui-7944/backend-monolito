package pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.AthleteProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

import java.util.Optional;

@Repository
public interface AthleteProfileRepository extends JpaRepository<AthleteProfile, Long> {
    Optional<AthleteProfile> findByUserId(UserId userId);
    boolean existsByUserId(UserId userId);
}
