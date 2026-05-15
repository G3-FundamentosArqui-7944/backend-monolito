package pe.edu.upc.bodymatch.nutrition.interfaces.rest.transform;

import pe.edu.upc.bodymatch.nutrition.domain.model.entities.FoodDetection;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources.FoodDetectionResource;

public class FoodDetectionResourceFromEntityAssembler {
    public static FoodDetectionResource toResourceFromEntity(FoodDetection detection) {
        return new FoodDetectionResource(
                detection.getId(),
                detection.getFoodName(),
                detection.getPortionGrams(),
                MacroSummaryResourceFromVOAssembler.toResourceFromVO(detection.getMacros()),
                detection.getConfidence());
    }
}
