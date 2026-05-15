package pe.edu.upc.bodymatch.training.interfaces.rest.transform;

import pe.edu.upc.bodymatch.training.domain.model.aggregates.WorkoutSession;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.WorkoutSessionResource;

public class WorkoutSessionResourceFromEntityAssembler {
    public static WorkoutSessionResource toResourceFromEntity(WorkoutSession session) {
        var exercises = session.getExercises().stream()
                .map(ExerciseExecutionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new WorkoutSessionResource(
                session.getId(),
                session.getUserId().userId(),
                session.getTitle(),
                session.getStartedAt(),
                session.getCompletedAt(),
                session.getStatus().name(),
                session.getNotes(),
                session.totalVolume(),
                exercises);
    }
}
