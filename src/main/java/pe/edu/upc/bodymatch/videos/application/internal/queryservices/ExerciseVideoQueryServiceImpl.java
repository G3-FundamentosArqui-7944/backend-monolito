package pe.edu.upc.bodymatch.videos.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.videos.domain.model.aggregates.ExerciseVideo;
import pe.edu.upc.bodymatch.videos.domain.model.queries.GetAnalyzedVideosByUserIdQuery;
import pe.edu.upc.bodymatch.videos.domain.model.queries.GetVideoByIdQuery;
import pe.edu.upc.bodymatch.videos.domain.model.queries.GetVideosByUserIdQuery;
import pe.edu.upc.bodymatch.videos.domain.model.valueobjects.VideoProcessingStatus;
import pe.edu.upc.bodymatch.videos.domain.services.ExerciseVideoQueryService;
import pe.edu.upc.bodymatch.videos.infrastructure.persistence.jpa.repositories.ExerciseVideoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseVideoQueryServiceImpl implements ExerciseVideoQueryService {
    private final ExerciseVideoRepository videoRepository;

    public ExerciseVideoQueryServiceImpl(ExerciseVideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public Optional<ExerciseVideo> handle(GetVideoByIdQuery query) {
        return videoRepository.findById(query.videoId());
    }

    @Override
    public List<ExerciseVideo> handle(GetVideosByUserIdQuery query) {
        return videoRepository.findAllByUserIdOrderByCreatedAtDesc(query.userId());
    }

    @Override
    public List<ExerciseVideo> handle(GetAnalyzedVideosByUserIdQuery query) {
        return videoRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(
                query.userId(), VideoProcessingStatus.ANALYZED);
    }
}
