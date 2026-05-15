package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record CoachProfileResource(
        Long id,
        Long userId,
        String biography,
        int yearsOfExperience,
        BigDecimal hourlyRate,
        String currency,
        Set<String> specialties,
        boolean acceptingClients,
        BigDecimal averageRating,
        int totalReviews,
        List<AvailabilitySlotResource> availabilitySlots) {
}
