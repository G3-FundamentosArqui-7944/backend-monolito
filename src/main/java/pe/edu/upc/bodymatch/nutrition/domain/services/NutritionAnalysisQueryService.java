package pe.edu.upc.bodymatch.nutrition.domain.services;

import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionAnalysis;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetNutritionAnalysesByUserQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetNutritionAnalysisByIdQuery;

import java.util.List;
import java.util.Optional;

public interface NutritionAnalysisQueryService {
    Optional<NutritionAnalysis> handle(GetNutritionAnalysisByIdQuery query);
    List<NutritionAnalysis> handle(GetNutritionAnalysesByUserQuery query);
}
