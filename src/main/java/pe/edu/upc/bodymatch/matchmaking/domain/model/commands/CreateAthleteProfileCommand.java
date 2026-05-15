package pe.edu.upc.bodymatch.matchmaking.domain.model.commands;

import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.FitnessGoal;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.TrainingLevel;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

import java.util.Set;

public record CreateAthleteProfileCommand(
        UserId userId,
        TrainingLevel trainingLevel,
        Set<FitnessGoal> goals,
        String preferences) {
}
