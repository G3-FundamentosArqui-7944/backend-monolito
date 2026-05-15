package pe.edu.upc.bodymatch.nutrition.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionPlan;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;

import java.util.Optional;

@Repository
public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
    Optional<NutritionPlan> findByUserIdAndActiveTrue(UserId userId);
}
