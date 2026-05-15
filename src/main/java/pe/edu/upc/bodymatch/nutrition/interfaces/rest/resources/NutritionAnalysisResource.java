package pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;

public record NutritionAnalysisResource(
        Long id,
        Long userId,
        String imageStorageUrl,
        String summary,
        MacroSummaryResource totalMacros,
        String status,
        String failureReason,
        String aiModelVersion,
        Instant analyzedAt,
        List<FoodDetectionResource> detectedFoods) {
}
