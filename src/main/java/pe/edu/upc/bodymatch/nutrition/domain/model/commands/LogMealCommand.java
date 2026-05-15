package pe.edu.upc.bodymatch.nutrition.domain.model.commands;

import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.MacroSummary;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.MealType;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;

import java.time.Instant;

public record LogMealCommand(
        UserId userId,
        MealType mealType,
        String description,
        MacroSummary macros,
        Instant consumedAt,
        Long sourceAnalysisId) {
}
