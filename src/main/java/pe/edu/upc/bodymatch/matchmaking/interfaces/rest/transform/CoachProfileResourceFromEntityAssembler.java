package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.CoachProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.Specialty;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.CoachProfileResource;

import java.util.stream.Collectors;

public class CoachProfileResourceFromEntityAssembler {
    public static CoachProfileResource toResourceFromEntity(CoachProfile coach) {
        var specialties = coach.getSpecialties().stream().map(Specialty::name).collect(Collectors.toSet());
        var slots = coach.getAvailabilitySlots().stream()
                .map(AvailabilitySlotResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new CoachProfileResource(
                coach.getId(),
                coach.getUserId().userId(),
                coach.getBiography(),
                coach.getYearsOfExperience(),
                coach.getHourlyRate(),
                coach.getCurrency(),
                specialties,
                coach.isAcceptingClients(),
                coach.getAverageRating(),
                coach.getTotalReviews(),
                slots);
    }
}
