package pe.edu.upc.bodymatch.matchmaking.domain.model.commands;

import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.Specialty;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

import java.math.BigDecimal;
import java.util.Set;

public record CreateCoachProfileCommand(
        UserId userId,
        String biography,
        int yearsOfExperience,
        BigDecimal hourlyRate,
        String currency,
        Set<Specialty> specialties) {
}
