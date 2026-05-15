package pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources;

import java.time.Instant;

public record CreateNutritionPlanResource(
        Long userId,
        String name,
        String description,
        MacroSummaryResource dailyTargets,
        Instant startDate,
        Instant endDate) {
}
