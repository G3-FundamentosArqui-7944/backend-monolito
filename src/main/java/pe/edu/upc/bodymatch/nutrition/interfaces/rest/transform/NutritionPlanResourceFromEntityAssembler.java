package pe.edu.upc.bodymatch.nutrition.interfaces.rest.transform;

import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionPlan;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources.NutritionPlanResource;

public class NutritionPlanResourceFromEntityAssembler {
    public static NutritionPlanResource toResourceFromEntity(NutritionPlan plan) {
        return new NutritionPlanResource(
                plan.getId(),
                plan.getUserId().userId(),
                plan.getName(),
                plan.getDescription(),
                MacroSummaryResourceFromVOAssembler.toResourceFromVO(plan.getDailyTargets()),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.isActive());
    }
}
