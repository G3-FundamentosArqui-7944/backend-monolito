package pe.edu.upc.bodymatch.matchmaking.domain.model.queries;

import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.Specialty;

import java.math.BigDecimal;
import java.util.Set;

public record SearchCoachesQuery(
        Set<Specialty> specialties,
        Integer minYearsOfExperience,
        BigDecimal maxHourlyRate,
        BigDecimal minRating,
        boolean onlyAccepting) {
}
