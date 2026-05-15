package pe.edu.upc.bodymatch.nutrition.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetActiveNutritionPlanByUserQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetDailyMacroSummaryQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.MacroSummary;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.nutrition.domain.services.MealRecordQueryService;
import pe.edu.upc.bodymatch.nutrition.domain.services.NutritionPlanQueryService;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class NutritionContextFacade {
    private final MealRecordQueryService mealRecordQueryService;
    private final NutritionPlanQueryService nutritionPlanQueryService;

    public NutritionContextFacade(MealRecordQueryService mealRecordQueryService,
                                  NutritionPlanQueryService nutritionPlanQueryService) {
        this.mealRecordQueryService = mealRecordQueryService;
        this.nutritionPlanQueryService = nutritionPlanQueryService;
    }

    public MacroSummary fetchDailyMacroSummary(Long userId, LocalDate date) {
        return mealRecordQueryService.handle(new GetDailyMacroSummaryQuery(new UserId(userId), date));
    }

    public Optional<MacroSummary> fetchActivePlanTargets(Long userId) {
        return nutritionPlanQueryService.handle(new GetActiveNutritionPlanByUserQuery(new UserId(userId)))
                .map(p -> p.getDailyTargets());
    }
}
