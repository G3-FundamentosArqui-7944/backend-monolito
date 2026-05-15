package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.TrainingSession;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.TrainingSessionResource;

public class TrainingSessionResourceFromEntityAssembler {
    public static TrainingSessionResource toResourceFromEntity(TrainingSession session) {
        return new TrainingSessionResource(
                session.getId(),
                session.getAthleteId().userId(),
                session.getCoachId().userId(),
                session.getScheduledAt(),
                session.getDurationMinutes(),
                session.getLocation(),
                session.getNotes(),
                session.getStatus().name(),
                session.getCompletedAt());
    }
}
