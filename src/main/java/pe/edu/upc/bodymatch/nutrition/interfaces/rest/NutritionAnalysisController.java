package pe.edu.upc.bodymatch.nutrition.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.AnalyzeFoodImageCommand;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetNutritionAnalysesByUserQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetNutritionAnalysisByIdQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.nutrition.domain.services.NutritionAnalysisCommandService;
import pe.edu.upc.bodymatch.nutrition.domain.services.NutritionAnalysisQueryService;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.resources.NutritionAnalysisResource;
import pe.edu.upc.bodymatch.nutrition.interfaces.rest.transform.NutritionAnalysisResourceFromEntityAssembler;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/nutrition/analyses", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Nutrition Analyses", description = "Food image analysis via Gemini AI")
public class NutritionAnalysisController {
    private final NutritionAnalysisCommandService nutritionAnalysisCommandService;
    private final NutritionAnalysisQueryService nutritionAnalysisQueryService;

    public NutritionAnalysisController(NutritionAnalysisCommandService nutritionAnalysisCommandService,
                                       NutritionAnalysisQueryService nutritionAnalysisQueryService) {
        this.nutritionAnalysisCommandService = nutritionAnalysisCommandService;
        this.nutritionAnalysisQueryService = nutritionAnalysisQueryService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NutritionAnalysisResource> analyzeImage(
            @RequestParam("userId") Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            byte[] content = file.getBytes();
            var command = new AnalyzeFoodImageCommand(
                    new UserId(userId),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    content);
            var analysis = nutritionAnalysisCommandService.handle(command);
            if (analysis.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    NutritionAnalysisResourceFromEntityAssembler.toResourceFromEntity(analysis.get()),
                    HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{analysisId}")
    public ResponseEntity<NutritionAnalysisResource> getById(@PathVariable Long analysisId) {
        var analysis = nutritionAnalysisQueryService.handle(new GetNutritionAnalysisByIdQuery(analysisId));
        if (analysis.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(NutritionAnalysisResourceFromEntityAssembler.toResourceFromEntity(analysis.get()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NutritionAnalysisResource>> byUser(@PathVariable Long userId) {
        var analyses = nutritionAnalysisQueryService.handle(new GetNutritionAnalysesByUserQuery(new UserId(userId)));
        return ResponseEntity.ok(analyses.stream()
                .map(NutritionAnalysisResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }
}
