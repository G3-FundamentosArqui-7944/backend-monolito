package pe.edu.upc.bodymatch.videos.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.videos.domain.model.aggregates.ExerciseVideo;
import pe.edu.upc.bodymatch.videos.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.videos.domain.model.valueobjects.VideoProcessingStatus;

import java.util.List;

@Repository
public interface ExerciseVideoRepository extends JpaRepository<ExerciseVideo, Long> {
    List<ExerciseVideo> findAllByUserIdOrderByCreatedAtDesc(UserId userId);
    List<ExerciseVideo> findAllByUserIdAndStatusOrderByCreatedAtDesc(UserId userId, VideoProcessingStatus status);
}
