package pe.edu.upc.bodymatch.nutrition.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.MealRecord;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;

import java.time.Instant;
import java.util.List;

@Repository
public interface MealRecordRepository extends JpaRepository<MealRecord, Long> {
    List<MealRecord> findAllByUserIdAndConsumedAtBetweenOrderByConsumedAtDesc(UserId userId, Instant from, Instant to);
    List<MealRecord> findAllByUserIdOrderByConsumedAtDesc(UserId userId);
}
