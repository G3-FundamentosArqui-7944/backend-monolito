package pe.edu.upc.bodymatch.matchmaking.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.TrainingSession;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetTrainingSessionsByAthleteQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetTrainingSessionsByCoachQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.services.TrainingSessionQueryService;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.TrainingSessionRepository;

import java.util.List;

@Service
public class TrainingSessionQueryServiceImpl implements TrainingSessionQueryService {
    private final TrainingSessionRepository trainingSessionRepository;

    public TrainingSessionQueryServiceImpl(TrainingSessionRepository trainingSessionRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
    }

    @Override
    public List<TrainingSession> handle(GetTrainingSessionsByAthleteQuery query) {
        return trainingSessionRepository.findAllByAthleteIdOrderByScheduledAtDesc(query.athleteId());
    }

    @Override
    public List<TrainingSession> handle(GetTrainingSessionsByCoachQuery query) {
        return trainingSessionRepository.findAllByCoachIdOrderByScheduledAtDesc(query.coachId());
    }
}
