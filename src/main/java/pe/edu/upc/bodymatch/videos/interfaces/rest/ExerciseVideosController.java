package pe.edu.upc.bodymatch.videos.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.bodymatch.videos.domain.model.commands.AnalyzeExerciseVideoCommand;
import pe.edu.upc.bodymatch.videos.domain.model.commands.DeleteExerciseVideoCommand;
import pe.edu.upc.bodymatch.videos.domain.model.commands.UploadExerciseVideoCommand;
import pe.edu.upc.bodymatch.videos.domain.model.queries.GetAnalyzedVideosByUserIdQuery;
import pe.edu.upc.bodymatch.videos.domain.model.queries.GetVideoByIdQuery;
import pe.edu.upc.bodymatch.videos.domain.model.queries.GetVideosByUserIdQuery;
import pe.edu.upc.bodymatch.videos.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.videos.domain.services.ExerciseVideoCommandService;
import pe.edu.upc.bodymatch.videos.domain.services.ExerciseVideoQueryService;
import pe.edu.upc.bodymatch.videos.interfaces.rest.resources.ExerciseVideoResource;
import pe.edu.upc.bodymatch.videos.interfaces.rest.transform.ExerciseVideoResourceFromEntityAssembler;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/exercise-videos", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Exercise Videos", description = "Upload, analyze and retrieve exercise execution videos")
public class ExerciseVideosController {
    private final ExerciseVideoCommandService videoCommandService;
    private final ExerciseVideoQueryService videoQueryService;

    public ExerciseVideosController(ExerciseVideoCommandService videoCommandService,
                                    ExerciseVideoQueryService videoQueryService) {
        this.videoCommandService = videoCommandService;
        this.videoQueryService = videoQueryService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExerciseVideoResource> upload(
            @RequestParam("userId") Long userId,
            @RequestParam("exerciseName") String exerciseName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "durationSeconds", required = false) Integer durationSeconds,
            @RequestParam("file") MultipartFile file) {
        try {
            byte[] content = file.getBytes();
            var command = new UploadExerciseVideoCommand(
                    new UserId(userId),
                    exerciseName,
                    description,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    durationSeconds,
                    content);
            var video = videoCommandService.handle(command);
            if (video.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    ExerciseVideoResourceFromEntityAssembler.toResourceFromEntity(video.get()),
                    HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{videoId}/analyze")
    public ResponseEntity<?> analyze(@PathVariable Long videoId) {
        try {
            var video = videoCommandService.handle(new AnalyzeExerciseVideoCommand(videoId));
            if (video.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(ExerciseVideoResourceFromEntityAssembler.toResourceFromEntity(video.get()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(
                    java.util.Map.of("message", e.getClass().getSimpleName() + ": " + e.getMessage()));
        }
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<ExerciseVideoResource> getById(@PathVariable Long videoId) {
        var video = videoQueryService.handle(new GetVideoByIdQuery(videoId));
        if (video.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ExerciseVideoResourceFromEntityAssembler.toResourceFromEntity(video.get()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExerciseVideoResource>> byUser(@PathVariable Long userId) {
        var videos = videoQueryService.handle(new GetVideosByUserIdQuery(new UserId(userId)));
        return ResponseEntity.ok(videos.stream()
                .map(ExerciseVideoResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/user/{userId}/analyzed")
    public ResponseEntity<List<ExerciseVideoResource>> analyzedByUser(@PathVariable Long userId) {
        var videos = videoQueryService.handle(new GetAnalyzedVideosByUserIdQuery(new UserId(userId)));
        return ResponseEntity.ok(videos.stream()
                .map(ExerciseVideoResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> delete(@PathVariable Long videoId) {
        try {
            videoCommandService.handle(new DeleteExerciseVideoCommand(videoId));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
