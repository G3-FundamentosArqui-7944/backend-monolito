package pe.edu.upc.bodymatch.matchmaking.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetCoachClientsQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.ConnectionRequestStatus;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.matchmaking.domain.services.ConnectionRequestQueryService;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.ConnectionRequestRepository;

import java.util.List;

@Service
public class MatchmakingContextFacade {
    private final ConnectionRequestQueryService connectionRequestQueryService;
    private final ConnectionRequestRepository connectionRequestRepository;

    public MatchmakingContextFacade(ConnectionRequestQueryService connectionRequestQueryService,
                                    ConnectionRequestRepository connectionRequestRepository) {
        this.connectionRequestQueryService = connectionRequestQueryService;
        this.connectionRequestRepository = connectionRequestRepository;
    }

    public boolean areConnected(Long athleteUserId, Long coachUserId) {
        return connectionRequestRepository.findByAthleteIdAndCoachIdAndStatus(
                new UserId(athleteUserId), new UserId(coachUserId), ConnectionRequestStatus.APPROVED).isPresent();
    }

    public List<Long> fetchCoachClientIds(Long coachUserId) {
        return connectionRequestQueryService.handle(new GetCoachClientsQuery(new UserId(coachUserId)))
                .stream()
                .map(r -> r.getAthleteId().userId())
                .toList();
    }
}
