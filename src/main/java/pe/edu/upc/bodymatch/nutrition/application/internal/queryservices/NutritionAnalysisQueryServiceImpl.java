package pe.edu.upc.bodymatch.nutrition.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionAnalysis;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetNutritionAnalysesByUserQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetNutritionAnalysisByIdQuery;
import pe.edu.upc.bodymatch.nutrition.domain.services.NutritionAnalysisQueryService;
import pe.edu.upc.bodymatch.nutrition.infrastructure.persistence.jpa.repositories.NutritionAnalysisRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NutritionAnalysisQueryServiceImpl implements NutritionAnalysisQueryService {
    private final NutritionAnalysisRepository nutritionAnalysisRepository;

    public NutritionAnalysisQueryServiceImpl(NutritionAnalysisRepository nutritionAnalysisRepository) {
        this.nutritionAnalysisRepository = nutritionAnalysisRepository;
    }

    @Override
    public Optional<NutritionAnalysis> handle(GetNutritionAnalysisByIdQuery query) {
        return nutritionAnalysisRepository.findById(query.analysisId());
    }

    @Override
    public List<NutritionAnalysis> handle(GetNutritionAnalysesByUserQuery query) {
        return nutritionAnalysisRepository.findAllByUserIdOrderByCreatedAtDesc(query.userId());
    }
}
