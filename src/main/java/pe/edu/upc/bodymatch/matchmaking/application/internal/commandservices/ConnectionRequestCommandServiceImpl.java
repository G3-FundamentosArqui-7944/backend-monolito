package pe.edu.upc.bodymatch.matchmaking.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.matchmaking.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.bodymatch.matchmaking.application.internal.outboundservices.acl.ExternalMembershipService;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.ConnectionRequest;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.RequestCoachingCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.RespondConnectionRequestCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.ConnectionRequestStatus;
import pe.edu.upc.bodymatch.matchmaking.domain.services.ConnectionRequestCommandService;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.CoachProfileRepository;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.ConnectionRequestRepository;

import java.util.Optional;

@Service
public class ConnectionRequestCommandServiceImpl implements ConnectionRequestCommandService {

    private final ConnectionRequestRepository connectionRequestRepository;
    private final CoachProfileRepository coachProfileRepository;
    private final ExternalIamService externalIamService;
    private final ExternalMembershipService externalMembershipService;

    public ConnectionRequestCommandServiceImpl(ConnectionRequestRepository connectionRequestRepository,
                                               CoachProfileRepository coachProfileRepository,
                                               ExternalIamService externalIamService,
                                               ExternalMembershipService externalMembershipService) {
        this.connectionRequestRepository = connectionRequestRepository;
        this.coachProfileRepository = coachProfileRepository;
        this.externalIamService = externalIamService;
        this.externalMembershipService = externalMembershipService;
    }

    @Override
    @Transactional
    public Optional<ConnectionRequest> handle(RequestCoachingCommand command) {
        if (!externalIamService.isAthlete(command.athleteId())) {
            throw new IllegalStateException("Requester is not an athlete");
        }
        if (!externalMembershipService.hasActiveMembership(command.athleteId())) {
            throw new IllegalStateException("Athlete must have an active membership to request coaching");
        }
        var coach = coachProfileRepository.findByUserId(command.coachId())
                .orElseThrow(() -> new IllegalArgumentException("Coach profile not found"));
        if (!coach.isAcceptingClients()) {
            throw new IllegalStateException("Coach is not accepting new clients");
        }
        var existingPending = connectionRequestRepository.findByAthleteIdAndCoachIdAndStatus(
                command.athleteId(), command.coachId(), ConnectionRequestStatus.PENDING);
        if (existingPending.isPresent()) {
            throw new IllegalStateException("A pending request already exists for this coach");
        }
        var existingApproved = connectionRequestRepository.findByAthleteIdAndCoachIdAndStatus(
                command.athleteId(), command.coachId(), ConnectionRequestStatus.APPROVED);
        if (existingApproved.isPresent()) {
            throw new IllegalStateException("Athlete is already connected with this coach");
        }

        var request = new ConnectionRequest(command.athleteId(), command.coachId(), command.message());
        connectionRequestRepository.save(request);
        return Optional.of(request);
    }

    @Override
    @Transactional
    public Optional<ConnectionRequest> handle(RespondConnectionRequestCommand command) {
        var request = connectionRequestRepository.findById(command.requestId())
                .orElseThrow(() -> new IllegalArgumentException("Connection request not found"));
        if (command.approve()) {
            request.approve(command.responseNote());
        } else {
            request.reject(command.responseNote());
        }
        connectionRequestRepository.save(request);
        return Optional.of(request);
    }
}
