package pe.edu.upc.bodymatch.nutrition.domain.services;

import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionPlan;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.CreateNutritionPlanCommand;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.DeactivateNutritionPlanCommand;

import java.util.Optional;

public interface NutritionPlanCommandService {
    Optional<NutritionPlan> handle(CreateNutritionPlanCommand command);
    Optional<NutritionPlan> handle(DeactivateNutritionPlanCommand command);
}
