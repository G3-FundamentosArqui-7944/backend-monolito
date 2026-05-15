package pe.edu.upc.bodymatch.nutrition.application.internal.outboundservices.ai.dto;

import java.math.BigDecimal;
import java.util.List;

public record NutritionAnalysisResult(
        String summary,
        String aiModelVersion,
        List<DetectedFood> detectedFoods) {

    public record DetectedFood(
            String foodName,
            BigDecimal portionGrams,
            BigDecimal calories,
            BigDecimal proteinGrams,
            BigDecimal carbohydratesGrams,
            BigDecimal fatGrams,
            BigDecimal fiberGrams,
            BigDecimal confidence) {
    }
}
