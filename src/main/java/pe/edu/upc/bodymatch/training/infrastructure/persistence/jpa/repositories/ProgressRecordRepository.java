package pe.edu.upc.bodymatch.training.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.training.domain.model.aggregates.ProgressRecord;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;

import java.util.List;

@Repository
public interface ProgressRecordRepository extends JpaRepository<ProgressRecord, Long> {
    List<ProgressRecord> findAllByUserIdOrderByAchievedAtDesc(UserId userId);
}
