package pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources;

import java.math.BigDecimal;

public record FoodDetectionResource(
        Long id,
        String foodName,
        BigDecimal portionGrams,
        MacroSummaryResource macros,
        BigDecimal confidence) {
}
