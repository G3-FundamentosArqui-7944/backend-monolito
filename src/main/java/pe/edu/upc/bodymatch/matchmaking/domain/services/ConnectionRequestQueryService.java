package pe.edu.upc.bodymatch.matchmaking.domain.services;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.ConnectionRequest;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetConnectionRequestsByAthleteQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetConnectionRequestsByCoachQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetCoachClientsQuery;

import java.util.List;

public interface ConnectionRequestQueryService {
    List<ConnectionRequest> handle(GetConnectionRequestsByAthleteQuery query);
    List<ConnectionRequest> handle(GetConnectionRequestsByCoachQuery query);
    List<ConnectionRequest> handle(GetCoachClientsQuery query);
}
