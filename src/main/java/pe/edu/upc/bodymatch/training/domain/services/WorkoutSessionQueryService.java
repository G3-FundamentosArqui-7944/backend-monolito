package pe.edu.upc.bodymatch.training.domain.services;

import pe.edu.upc.bodymatch.training.domain.model.aggregates.WorkoutSession;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetWorkoutSessionByIdQuery;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetWorkoutSessionsByUserQuery;

import java.util.List;
import java.util.Optional;

public interface WorkoutSessionQueryService {
    List<WorkoutSession> handle(GetWorkoutSessionsByUserQuery query);
    Optional<WorkoutSession> handle(GetWorkoutSessionByIdQuery query);
}
