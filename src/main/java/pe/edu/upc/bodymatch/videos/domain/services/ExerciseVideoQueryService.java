package pe.edu.upc.bodymatch.videos.domain.services;

import pe.edu.upc.bodymatch.videos.domain.model.aggregates.ExerciseVideo;
import pe.edu.upc.bodymatch.videos.domain.model.queries.GetAnalyzedVideosByUserIdQuery;
import pe.edu.upc.bodymatch.videos.domain.model.queries.GetVideoByIdQuery;
import pe.edu.upc.bodymatch.videos.domain.model.queries.GetVideosByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface ExerciseVideoQueryService {
    Optional<ExerciseVideo> handle(GetVideoByIdQuery query);
    List<ExerciseVideo> handle(GetVideosByUserIdQuery query);
    List<ExerciseVideo> handle(GetAnalyzedVideosByUserIdQuery query);
}
