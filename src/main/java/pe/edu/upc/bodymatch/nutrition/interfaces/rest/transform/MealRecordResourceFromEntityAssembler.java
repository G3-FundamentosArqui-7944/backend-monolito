package pe.edu.upc.bodymatch.nutrition.interfaces.rest.transform;

import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.MealRecord;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources.MealRecordResource;

public class MealRecordResourceFromEntityAssembler {
    public static MealRecordResource toResourceFromEntity(MealRecord meal) {
        return new MealRecordResource(
                meal.getId(),
                meal.getUserId().userId(),
                meal.getMealType().name(),
                meal.getDescription(),
                MacroSummaryResourceFromVOAssembler.toResourceFromVO(meal.getMacros()),
                meal.getConsumedAt(),
                meal.getSourceAnalysisId());
    }
}
