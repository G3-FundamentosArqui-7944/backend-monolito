package pe.edu.upc.bodymatch.nutrition.domain.services;

import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionAnalysis;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.AnalyzeFoodImageCommand;

import java.util.Optional;

public interface NutritionAnalysisCommandService {
    Optional<NutritionAnalysis> handle(AnalyzeFoodImageCommand command);
}
