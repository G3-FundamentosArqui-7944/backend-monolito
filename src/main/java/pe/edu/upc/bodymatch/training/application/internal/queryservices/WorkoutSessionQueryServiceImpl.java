package pe.edu.upc.bodymatch.training.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.WorkoutSession;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetWorkoutSessionByIdQuery;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetWorkoutSessionsByUserQuery;
import pe.edu.upc.bodymatch.training.domain.services.WorkoutSessionQueryService;
import pe.edu.upc.bodymatch.training.infrastructure.persistence.jpa.repositories.WorkoutSessionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutSessionQueryServiceImpl implements WorkoutSessionQueryService {
    private final WorkoutSessionRepository workoutSessionRepository;

    public WorkoutSessionQueryServiceImpl(WorkoutSessionRepository workoutSessionRepository) {
        this.workoutSessionRepository = workoutSessionRepository;
    }

    @Override
    public List<WorkoutSession> handle(GetWorkoutSessionsByUserQuery query) {
        return workoutSessionRepository.findAllByUserIdOrderByStartedAtDesc(query.userId());
    }

    @Override
    public Optional<WorkoutSession> handle(GetWorkoutSessionByIdQuery query) {
        return workoutSessionRepository.findById(query.workoutSessionId());
    }
}
