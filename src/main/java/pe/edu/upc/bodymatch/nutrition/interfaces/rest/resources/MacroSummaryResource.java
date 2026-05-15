package pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources;

import java.math.BigDecimal;

public record MacroSummaryResource(
        BigDecimal calories,
        BigDecimal proteinGrams,
        BigDecimal carbohydratesGrams,
        BigDecimal fatGrams,
        BigDecimal fiberGrams) {
}
