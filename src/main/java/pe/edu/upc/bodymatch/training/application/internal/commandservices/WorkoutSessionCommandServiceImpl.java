package pe.edu.upc.bodymatch.training.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.training.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.WorkoutSession;
import pe.edu.upc.bodymatch.training.domain.model.commands.AddExerciseExecutionCommand;
import pe.edu.upc.bodymatch.training.domain.model.commands.CompleteWorkoutSessionCommand;
import pe.edu.upc.bodymatch.training.domain.model.commands.StartWorkoutSessionCommand;
import pe.edu.upc.bodymatch.training.domain.model.entities.ExerciseExecution;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.Weight;
import pe.edu.upc.bodymatch.training.domain.services.WorkoutSessionCommandService;
import pe.edu.upc.bodymatch.training.infrastructure.persistence.jpa.repositories.WorkoutSessionRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class WorkoutSessionCommandServiceImpl implements WorkoutSessionCommandService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExternalIamService externalIamService;

    public WorkoutSessionCommandServiceImpl(WorkoutSessionRepository workoutSessionRepository,
                                            ExternalIamService externalIamService) {
        this.workoutSessionRepository = workoutSessionRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public Optional<WorkoutSession> handle(StartWorkoutSessionCommand command) {
        if (!externalIamService.existsUser(command.userId())) {
            throw new IllegalArgumentException("User does not exist: " + command.userId().userId());
        }
        var startedAt = command.startedAt() != null ? command.startedAt() : Instant.now();
        var session = new WorkoutSession(command.userId(), command.title(), startedAt, command.notes());
        workoutSessionRepository.save(session);
        return Optional.of(session);
    }

    @Override
    @Transactional
    public Optional<WorkoutSession> handle(AddExerciseExecutionCommand command) {
        var session = workoutSessionRepository.findById(command.workoutSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Workout session not found"));
        var exercise = new ExerciseExecution(
                command.exerciseName(),
                command.sets(),
                command.reps(),
                new Weight(command.load(), command.loadUnit()),
                command.durationSeconds(),
                command.restSeconds(),
                command.notes());
        session.addExercise(exercise);
        workoutSessionRepository.save(session);
        return Optional.of(session);
    }

    @Override
    @Transactional
    public Optional<WorkoutSession> handle(CompleteWorkoutSessionCommand command) {
        var session = workoutSessionRepository.findById(command.workoutSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Workout session not found"));
        session.complete();
        workoutSessionRepository.save(session);
        return Optional.of(session);
    }
}
