package pe.edu.upc.bodymatch.matchmaking.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.ConnectionRequest;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetCoachClientsQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetConnectionRequestsByAthleteQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetConnectionRequestsByCoachQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.ConnectionRequestStatus;
import pe.edu.upc.bodymatch.matchmaking.domain.services.ConnectionRequestQueryService;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.ConnectionRequestRepository;

import java.util.List;

@Service
public class ConnectionRequestQueryServiceImpl implements ConnectionRequestQueryService {
    private final ConnectionRequestRepository connectionRequestRepository;

    public ConnectionRequestQueryServiceImpl(ConnectionRequestRepository connectionRequestRepository) {
        this.connectionRequestRepository = connectionRequestRepository;
    }

    @Override
    public List<ConnectionRequest> handle(GetConnectionRequestsByAthleteQuery query) {
        return connectionRequestRepository.findAllByAthleteIdOrderByCreatedAtDesc(query.athleteId());
    }

    @Override
    public List<ConnectionRequest> handle(GetConnectionRequestsByCoachQuery query) {
        return connectionRequestRepository.findAllByCoachIdOrderByCreatedAtDesc(query.coachId());
    }

    @Override
    public List<ConnectionRequest> handle(GetCoachClientsQuery query) {
        return connectionRequestRepository.findAllByCoachIdAndStatus(query.coachId(), ConnectionRequestStatus.APPROVED);
    }
}
