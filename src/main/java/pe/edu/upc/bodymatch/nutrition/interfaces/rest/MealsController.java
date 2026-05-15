package pe.edu.upc.bodymatch.nutrition.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.LogMealCommand;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetDailyMacroSummaryQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetMealsByUserQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.MealType;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.nutrition.domain.services.MealRecordCommandService;
import pe.edu.upc.bodymatch.nutrition.domain.services.MealRecordQueryService;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources.LogMealResource;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources.MacroSummaryResource;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources.MealRecordResource;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.transform.MacroSummaryResourceFromVOAssembler;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.transform.MealRecordResourceFromEntityAssembler;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/nutrition/meals", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Meals", description = "Meal logging and macro tracking")
public class MealsController {
    private final MealRecordCommandService mealRecordCommandService;
    private final MealRecordQueryService mealRecordQueryService;

    public MealsController(MealRecordCommandService mealRecordCommandService,
                           MealRecordQueryService mealRecordQueryService) {
        this.mealRecordCommandService = mealRecordCommandService;
        this.mealRecordQueryService = mealRecordQueryService;
    }

    @PostMapping
    public ResponseEntity<MealRecordResource> logMeal(@RequestBody LogMealResource resource) {
        try {
            var command = new LogMealCommand(
                    new UserId(resource.userId()),
                    MealType.valueOf(resource.mealType()),
                    resource.description(),
                    MacroSummaryResourceFromVOAssembler.toVOFromResource(resource.macros()),
                    resource.consumedAt(),
                    resource.sourceAnalysisId());
            var meal = mealRecordCommandService.handle(command);
            if (meal.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    MealRecordResourceFromEntityAssembler.toResourceFromEntity(meal.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MealRecordResource>> byUser(
            @PathVariable Long userId,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to) {
        var meals = mealRecordQueryService.handle(new GetMealsByUserQuery(new UserId(userId), from, to));
        return ResponseEntity.ok(meals.stream()
                .map(MealRecordResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/user/{userId}/daily-summary")
    public ResponseEntity<MacroSummaryResource> dailySummary(
            @PathVariable Long userId,
            @RequestParam(required = false) LocalDate date) {
        var summary = mealRecordQueryService.handle(new GetDailyMacroSummaryQuery(new UserId(userId), date));
        return ResponseEntity.ok(MacroSummaryResourceFromVOAssembler.toResourceFromVO(summary));
    }
}
