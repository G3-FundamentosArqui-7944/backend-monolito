package pe.edu.upc.bodymatch.matchmaking.domain.services;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.AthleteProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetAthleteByUserIdQuery;

import java.util.Optional;

public interface AthleteProfileQueryService {
    Optional<AthleteProfile> handle(GetAthleteByUserIdQuery query);
}
