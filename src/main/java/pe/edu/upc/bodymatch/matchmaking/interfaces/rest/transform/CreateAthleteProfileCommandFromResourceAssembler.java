package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform;

import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CreateAthleteProfileCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.FitnessGoal;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.TrainingLevel;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.CreateAthleteProfileResource;

import java.util.stream.Collectors;

public class CreateAthleteProfileCommandFromResourceAssembler {
    public static CreateAthleteProfileCommand toCommandFromResource(CreateAthleteProfileResource resource) {
        var goals = resource.goals().stream().map(FitnessGoal::valueOf).collect(Collectors.toSet());
        return new CreateAthleteProfileCommand(
                new UserId(resource.userId()),
                TrainingLevel.valueOf(resource.trainingLevel()),
                goals,
                resource.preferences());
    }
}
