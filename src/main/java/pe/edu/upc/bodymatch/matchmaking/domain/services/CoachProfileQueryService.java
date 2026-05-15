package pe.edu.upc.bodymatch.matchmaking.domain.services;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.CoachProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetCoachByUserIdQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetRecommendedCoachesQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.SearchCoachesQuery;

import java.util.List;
import java.util.Optional;

public interface CoachProfileQueryService {
    List<CoachProfile> handle(SearchCoachesQuery query);
    Optional<CoachProfile> handle(GetCoachByUserIdQuery query);
    List<CoachProfile> handle(GetRecommendedCoachesQuery query);
}
