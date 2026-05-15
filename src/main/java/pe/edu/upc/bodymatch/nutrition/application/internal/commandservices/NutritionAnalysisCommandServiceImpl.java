package pe.edu.upc.bodymatch.nutrition.application.internal.commandservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.nutrition.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.bodymatch.nutrition.application.internal.outboundservices.ai.NutritionAiAnalyzer;
import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionAnalysis;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.AnalyzeFoodImageCommand;
import pe.edu.upc.bodymatch.nutrition.domain.model.entities.FoodDetection;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.MacroSummary;
import pe.edu.upc.bodymatch.nutrition.domain.services.NutritionAnalysisCommandService;
import pe.edu.upc.bodymatch.nutrition.infrastructure.persistence.jpa.repositories.NutritionAnalysisRepository;
import pe.edu.upc.bodymatch.shared.infrastructure.storage.CloudStorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NutritionAnalysisCommandServiceImpl implements NutritionAnalysisCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NutritionAnalysisCommandServiceImpl.class);

    private final NutritionAnalysisRepository nutritionAnalysisRepository;
    private final CloudStorageService cloudStorageService;
    private final NutritionAiAnalyzer nutritionAiAnalyzer;
    private final ExternalIamService externalIamService;

    public NutritionAnalysisCommandServiceImpl(NutritionAnalysisRepository nutritionAnalysisRepository,
                                               CloudStorageService cloudStorageService,
                                               NutritionAiAnalyzer nutritionAiAnalyzer,
                                               ExternalIamService externalIamService) {
        this.nutritionAnalysisRepository = nutritionAnalysisRepository;
        this.cloudStorageService = cloudStorageService;
        this.nutritionAiAnalyzer = nutritionAiAnalyzer;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public Optional<NutritionAnalysis> handle(AnalyzeFoodImageCommand command) {
        if (!externalIamService.existsUser(command.userId())) {
            throw new IllegalArgumentException("User does not exist: " + command.userId().userId());
        }
        if (command.imageContent() == null || command.imageContent().length == 0) {
            throw new IllegalArgumentException("Image content is required");
        }
        var stored = cloudStorageService.upload(
                "nutrition/" + command.userId().userId(),
                command.originalFilename(),
                command.contentType(),
                command.imageContent());
        var analysis = new NutritionAnalysis(command.userId(), stored.storageKey(), stored.url());
        analysis.markProcessing();
        nutritionAnalysisRepository.save(analysis);

        try {
            var result = nutritionAiAnalyzer.analyzeFoodImage(command.contentType(), command.imageContent());
            List<FoodDetection> detections = new ArrayList<>();
            for (var detected : result.detectedFoods()) {
                detections.add(new FoodDetection(
                        detected.foodName(),
                        detected.portionGrams(),
                        new MacroSummary(
                                detected.calories(),
                                detected.proteinGrams(),
                                detected.carbohydratesGrams(),
                                detected.fatGrams(),
                                detected.fiberGrams()),
                        detected.confidence()));
            }
            analysis.completeWith(result.summary(), result.aiModelVersion(), detections);
            nutritionAnalysisRepository.save(analysis);
            return Optional.of(analysis);
        } catch (RuntimeException e) {
            LOGGER.error("Nutrition analysis failed: {}", e.getMessage());
            analysis.markFailed(e.getMessage());
            nutritionAnalysisRepository.save(analysis);
            return Optional.of(analysis);
        }
    }
}
