package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.Set;

public record CreateCoachProfileResource(
        Long userId,
        String biography,
        int yearsOfExperience,
        BigDecimal hourlyRate,
        String currency,
        Set<String> specialties) {
}
