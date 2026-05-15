package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.AthleteProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.FitnessGoal;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.AthleteProfileResource;

import java.util.stream.Collectors;

public class AthleteProfileResourceFromEntityAssembler {
    public static AthleteProfileResource toResourceFromEntity(AthleteProfile athlete) {
        var goals = athlete.getGoals().stream().map(FitnessGoal::name).collect(Collectors.toSet());
        return new AthleteProfileResource(
                athlete.getId(),
                athlete.getUserId().userId(),
                athlete.getTrainingLevel().name(),
                goals,
                athlete.getPreferences());
    }
}
