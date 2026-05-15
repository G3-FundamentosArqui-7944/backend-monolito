package pe.edu.upc.bodymatch.nutrition.interfaces.rest.transform;

import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionAnalysis;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources.NutritionAnalysisResource;

public class NutritionAnalysisResourceFromEntityAssembler {
    public static NutritionAnalysisResource toResourceFromEntity(NutritionAnalysis analysis) {
        var detected = analysis.getDetectedFoods().stream()
                .map(FoodDetectionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new NutritionAnalysisResource(
                analysis.getId(),
                analysis.getUserId().userId(),
                analysis.getImageStorageUrl(),
                analysis.getSummary(),
                MacroSummaryResourceFromVOAssembler.toResourceFromVO(analysis.getTotalMacros()),
                analysis.getStatus().name(),
                analysis.getFailureReason(),
                analysis.getAiModelVersion(),
                analysis.getAnalyzedAt(),
                detected);
    }
}
