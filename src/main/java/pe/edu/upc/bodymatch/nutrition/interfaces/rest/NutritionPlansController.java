package pe.edu.upc.bodymatch.nutrition.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.CreateNutritionPlanCommand;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.DeactivateNutritionPlanCommand;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetActiveNutritionPlanByUserQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.nutrition.domain.services.NutritionPlanCommandService;
import pe.edu.upc.bodymatch.nutrition.domain.services.NutritionPlanQueryService;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources.CreateNutritionPlanResource;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources.NutritionPlanResource;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.transform.MacroSummaryResourceFromVOAssembler;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.transform.NutritionPlanResourceFromEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/nutrition/plans", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Nutrition Plans", description = "Nutrition plan management")
public class NutritionPlansController {
    private final NutritionPlanCommandService nutritionPlanCommandService;
    private final NutritionPlanQueryService nutritionPlanQueryService;

    public NutritionPlansController(NutritionPlanCommandService nutritionPlanCommandService,
                                    NutritionPlanQueryService nutritionPlanQueryService) {
        this.nutritionPlanCommandService = nutritionPlanCommandService;
        this.nutritionPlanQueryService = nutritionPlanQueryService;
    }

    @PostMapping
    public ResponseEntity<NutritionPlanResource> create(@RequestBody CreateNutritionPlanResource resource) {
        try {
            var command = new CreateNutritionPlanCommand(
                    new UserId(resource.userId()),
                    resource.name(),
                    resource.description(),
                    MacroSummaryResourceFromVOAssembler.toVOFromResource(resource.dailyTargets()),
                    resource.startDate(),
                    resource.endDate());
            var plan = nutritionPlanCommandService.handle(command);
            if (plan.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    NutritionPlanResourceFromEntityAssembler.toResourceFromEntity(plan.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<NutritionPlanResource> activeByUser(@PathVariable Long userId) {
        var plan = nutritionPlanQueryService.handle(new GetActiveNutritionPlanByUserQuery(new UserId(userId)));
        if (plan.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(NutritionPlanResourceFromEntityAssembler.toResourceFromEntity(plan.get()));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<NutritionPlanResource> deactivate(@PathVariable Long planId) {
        try {
            var plan = nutritionPlanCommandService.handle(new DeactivateNutritionPlanCommand(planId));
            if (plan.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(NutritionPlanResourceFromEntityAssembler.toResourceFromEntity(plan.get()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
