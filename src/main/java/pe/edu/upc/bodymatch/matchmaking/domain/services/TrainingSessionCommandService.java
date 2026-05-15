package pe.edu.upc.bodymatch.matchmaking.domain.services;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.TrainingSession;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CancelTrainingSessionCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CompleteTrainingSessionCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.ScheduleTrainingSessionCommand;

import java.util.Optional;

public interface TrainingSessionCommandService {
    Optional<TrainingSession> handle(ScheduleTrainingSessionCommand command);
    Optional<TrainingSession> handle(CompleteTrainingSessionCommand command);
    Optional<TrainingSession> handle(CancelTrainingSessionCommand command);
}
