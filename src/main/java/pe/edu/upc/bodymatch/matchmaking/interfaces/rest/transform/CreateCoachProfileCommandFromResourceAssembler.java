package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform;

import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CreateCoachProfileCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.Specialty;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.CreateCoachProfileResource;

import java.util.stream.Collectors;

public class CreateCoachProfileCommandFromResourceAssembler {
    public static CreateCoachProfileCommand toCommandFromResource(CreateCoachProfileResource resource) {
        var specialties = resource.specialties().stream()
                .map(Specialty::valueOf)
                .collect(Collectors.toSet());
        return new CreateCoachProfileCommand(
                new UserId(resource.userId()),
                resource.biography(),
                resource.yearsOfExperience(),
                resource.hourlyRate(),
                resource.currency(),
                specialties);
    }
}
