package pe.edu.upc.bodymatch.matchmaking.domain.services;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.TrainingSession;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetTrainingSessionsByAthleteQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetTrainingSessionsByCoachQuery;

import java.util.List;

public interface TrainingSessionQueryService {
    List<TrainingSession> handle(GetTrainingSessionsByAthleteQuery query);
    List<TrainingSession> handle(GetTrainingSessionsByCoachQuery query);
}
