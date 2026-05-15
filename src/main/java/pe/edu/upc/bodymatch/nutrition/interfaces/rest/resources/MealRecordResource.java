package pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources;

import java.time.Instant;

public record MealRecordResource(
        Long id,
        Long userId,
        String mealType,
        String description,
        MacroSummaryResource macros,
        Instant consumedAt,
        Long sourceAnalysisId) {
}
