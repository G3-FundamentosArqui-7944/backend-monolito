package pe.edu.upc.bodymatch.nutrition.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionPlan;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetActiveNutritionPlanByUserQuery;
import pe.edu.upc.bodymatch.nutrition.domain.services.NutritionPlanQueryService;
import pe.edu.upc.bodymatch.nutrition.infrastructure.persistence.jpa.repositories.NutritionPlanRepository;

import java.util.Optional;

@Service
public class NutritionPlanQueryServiceImpl implements NutritionPlanQueryService {
    private final NutritionPlanRepository nutritionPlanRepository;

    public NutritionPlanQueryServiceImpl(NutritionPlanRepository nutritionPlanRepository) {
        this.nutritionPlanRepository = nutritionPlanRepository;
    }

    @Override
    public Optional<NutritionPlan> handle(GetActiveNutritionPlanByUserQuery query) {
        return nutritionPlanRepository.findByUserIdAndActiveTrue(query.userId());
    }
}
