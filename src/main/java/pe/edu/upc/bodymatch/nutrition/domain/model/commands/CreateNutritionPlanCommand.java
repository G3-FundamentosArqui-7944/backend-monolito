package pe.edu.upc.bodymatch.nutrition.domain.model.commands;

import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.MacroSummary;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;

import java.time.Instant;

public record CreateNutritionPlanCommand(
        UserId userId,
        String name,
        String description,
        MacroSummary dailyTargets,
        Instant startDate,
        Instant endDate) {
}
