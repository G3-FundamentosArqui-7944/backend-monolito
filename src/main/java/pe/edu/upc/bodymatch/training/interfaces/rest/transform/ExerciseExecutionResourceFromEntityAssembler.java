package pe.edu.upc.bodymatch.training.interfaces.rest.transform;

import pe.edu.upc.bodymatch.training.domain.model.entities.ExerciseExecution;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.ExerciseExecutionResource;

public class ExerciseExecutionResourceFromEntityAssembler {
    public static ExerciseExecutionResource toResourceFromEntity(ExerciseExecution exec) {
        return new ExerciseExecutionResource(
                exec.getId(),
                exec.getExerciseName(),
                exec.getSets(),
                exec.getReps(),
                exec.getLoad() == null ? null : exec.getLoad().value(),
                exec.getLoad() == null ? null : exec.getLoad().unit(),
                exec.getDurationSeconds(),
                exec.getRestSeconds(),
                exec.getNotes());
    }
}
