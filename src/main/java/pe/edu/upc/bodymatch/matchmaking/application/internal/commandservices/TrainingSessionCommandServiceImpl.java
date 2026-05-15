package pe.edu.upc.bodymatch.matchmaking.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.TrainingSession;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CancelTrainingSessionCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CompleteTrainingSessionCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.ScheduleTrainingSessionCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.ConnectionRequestStatus;
import pe.edu.upc.bodymatch.matchmaking.domain.services.TrainingSessionCommandService;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.ConnectionRequestRepository;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.TrainingSessionRepository;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class TrainingSessionCommandServiceImpl implements TrainingSessionCommandService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final ConnectionRequestRepository connectionRequestRepository;

    public TrainingSessionCommandServiceImpl(TrainingSessionRepository trainingSessionRepository,
                                             ConnectionRequestRepository connectionRequestRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.connectionRequestRepository = connectionRequestRepository;
    }

    @Override
    @Transactional
    public Optional<TrainingSession> handle(ScheduleTrainingSessionCommand command) {
        var approvedConnection = connectionRequestRepository.findByAthleteIdAndCoachIdAndStatus(
                command.athleteId(), command.coachId(), ConnectionRequestStatus.APPROVED);
        if (approvedConnection.isEmpty()) {
            throw new IllegalStateException("Athlete and coach are not connected");
        }
        var sessionEnd = command.scheduledAt().plus(command.durationMinutes(), ChronoUnit.MINUTES);
        var conflicting = trainingSessionRepository.findAllByCoachIdAndScheduledAtBetween(
                command.coachId(), command.scheduledAt(), sessionEnd);
        if (!conflicting.isEmpty()) {
            throw new IllegalStateException("Coach has a conflicting session at the requested time");
        }
        var session = new TrainingSession(
                command.athleteId(), command.coachId(),
                command.scheduledAt(), command.durationMinutes(),
                command.location(), command.notes());
        trainingSessionRepository.save(session);
        return Optional.of(session);
    }

    @Override
    @Transactional
    public Optional<TrainingSession> handle(CompleteTrainingSessionCommand command) {
        var session = trainingSessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new IllegalArgumentException("Training session not found"));
        session.complete(command.coachNotes());
        trainingSessionRepository.save(session);
        return Optional.of(session);
    }

    @Override
    @Transactional
    public Optional<TrainingSession> handle(CancelTrainingSessionCommand command) {
        var session = trainingSessionRepository.findById(command.sessionId())
                .orElseThrow(() -> new IllegalArgumentException("Training session not found"));
        session.cancel();
        trainingSessionRepository.save(session);
        return Optional.of(session);
    }
}
