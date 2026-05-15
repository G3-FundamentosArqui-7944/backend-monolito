package pe.edu.upc.bodymatch.nutrition.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionAnalysis;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;

import java.util.List;

@Repository
public interface NutritionAnalysisRepository extends JpaRepository<NutritionAnalysis, Long> {
    List<NutritionAnalysis> findAllByUserIdOrderByCreatedAtDesc(UserId userId);
}
