package pe.edu.upc.bodymatch.nutrition.domain.services;

import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.MealRecord;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetDailyMacroSummaryQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetMealsByUserQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.MacroSummary;

import java.util.List;

public interface MealRecordQueryService {
    List<MealRecord> handle(GetMealsByUserQuery query);
    MacroSummary handle(GetDailyMacroSummaryQuery query);
}
