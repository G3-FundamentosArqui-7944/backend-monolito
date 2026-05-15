package pe.edu.upc.bodymatch.matchmaking.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.AthleteProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetAthleteByUserIdQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.services.AthleteProfileQueryService;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.AthleteProfileRepository;

import java.util.Optional;

@Service
public class AthleteProfileQueryServiceImpl implements AthleteProfileQueryService {
    private final AthleteProfileRepository athleteProfileRepository;

    public AthleteProfileQueryServiceImpl(AthleteProfileRepository athleteProfileRepository) {
        this.athleteProfileRepository = athleteProfileRepository;
    }

    @Override
    public Optional<AthleteProfile> handle(GetAthleteByUserIdQuery query) {
        return athleteProfileRepository.findByUserId(query.userId());
    }
}
