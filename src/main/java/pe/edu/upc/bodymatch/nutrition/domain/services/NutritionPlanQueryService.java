package pe.edu.upc.bodymatch.nutrition.domain.services;

import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionPlan;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetActiveNutritionPlanByUserQuery;

import java.util.Optional;

public interface NutritionPlanQueryService {
    Optional<NutritionPlan> handle(GetActiveNutritionPlanByUserQuery query);
}
