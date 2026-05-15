package pe.edu.upc.bodymatch.nutrition.interfaces.rest.transform;

import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.MacroSummary;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources.MacroSummaryResource;

public class MacroSummaryResourceFromVOAssembler {
    public static MacroSummaryResource toResourceFromVO(MacroSummary macros) {
        if (macros == null) return null;
        return new MacroSummaryResource(
                macros.calories(),
                macros.proteinGrams(),
                macros.carbohydratesGrams(),
                macros.fatGrams(),
                macros.fiberGrams());
    }

    public static MacroSummary toVOFromResource(MacroSummaryResource resource) {
        if (resource == null) return new MacroSummary();
        return new MacroSummary(
                resource.calories(),
                resource.proteinGrams(),
                resource.carbohydratesGrams(),
                resource.fatGrams(),
                resource.fiberGrams());
    }
}
