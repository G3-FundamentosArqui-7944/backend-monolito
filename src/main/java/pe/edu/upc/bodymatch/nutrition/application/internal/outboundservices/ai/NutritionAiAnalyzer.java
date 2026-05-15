package pe.edu.upc.bodymatch.nutrition.application.internal.outboundservices.ai;

import pe.edu.upc.bodymatch.nutrition.application.internal.outboundservices.ai.dto.NutritionAnalysisResult;

public interface NutritionAiAnalyzer {
    NutritionAnalysisResult analyzeFoodImage(String mimeType, byte[] imageContent);
}
