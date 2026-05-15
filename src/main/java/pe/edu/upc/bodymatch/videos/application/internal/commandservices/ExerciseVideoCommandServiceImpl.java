package pe.edu.upc.bodymatch.videos.application.internal.commandservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.shared.infrastructure.storage.CloudStorageService;
import pe.edu.upc.bodymatch.videos.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.bodymatch.videos.application.internal.outboundservices.acl.ExternalMembershipService;
import pe.edu.upc.bodymatch.videos.application.internal.outboundservices.ai.ExerciseVideoAiAnalyzer;
import pe.edu.upc.bodymatch.videos.application.internal.outboundservices.ai.dto.ExerciseAnalysisResult;
import pe.edu.upc.bodymatch.videos.domain.model.aggregates.ExerciseVideo;
import pe.edu.upc.bodymatch.videos.domain.model.commands.AnalyzeExerciseVideoCommand;
import pe.edu.upc.bodymatch.videos.domain.model.commands.DeleteExerciseVideoCommand;
import pe.edu.upc.bodymatch.videos.domain.model.commands.UploadExerciseVideoCommand;
import pe.edu.upc.bodymatch.videos.domain.model.entities.TechnicalFeedback;
import pe.edu.upc.bodymatch.videos.domain.model.entities.VideoAnalysis;
import pe.edu.upc.bodymatch.videos.domain.model.valueobjects.FeedbackSeverity;
import pe.edu.upc.bodymatch.videos.domain.services.ExerciseVideoCommandService;
import pe.edu.upc.bodymatch.videos.infrastructure.persistence.jpa.repositories.ExerciseVideoRepository;

import java.util.Optional;

@Service
public class ExerciseVideoCommandServiceImpl implements ExerciseVideoCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExerciseVideoCommandServiceImpl.class);

    private final ExerciseVideoRepository videoRepository;
    private final CloudStorageService cloudStorageService;
    private final ExerciseVideoAiAnalyzer aiAnalyzer;
    private final ExternalIamService externalIamService;
    private final ExternalMembershipService externalMembershipService;

    public ExerciseVideoCommandServiceImpl(ExerciseVideoRepository videoRepository,
                                           CloudStorageService cloudStorageService,
                                           ExerciseVideoAiAnalyzer aiAnalyzer,
                                           ExternalIamService externalIamService,
                                           ExternalMembershipService externalMembershipService) {
        this.videoRepository = videoRepository;
        this.cloudStorageService = cloudStorageService;
        this.aiAnalyzer = aiAnalyzer;
        this.externalIamService = externalIamService;
        this.externalMembershipService = externalMembershipService;
    }

    @Override
    @Transactional
    public Optional<ExerciseVideo> handle(UploadExerciseVideoCommand command) {
        if (!externalIamService.existsUser(command.userId())) {
            throw new IllegalArgumentException("User does not exist: " + command.userId().userId());
        }
        // Membership gate disabled for development. Re-enable in production.
        // if (!externalMembershipService.hasActiveMembership(command.userId())) {
        //     throw new IllegalStateException("Active membership is required to upload exercise videos");
        // }
        if (command.content() == null || command.content().length == 0) {
            throw new IllegalArgumentException("Video content cannot be empty");
        }
        var resolvedContentType = resolveVideoMimeType(command.contentType(), command.originalFilename());
        var stored = cloudStorageService.upload(
                "videos/" + command.userId().userId(),
                command.originalFilename(),
                resolvedContentType,
                command.content());
        var video = new ExerciseVideo(
                command.userId(),
                command.exerciseName(),
                command.description(),
                stored.storageKey(),
                stored.url(),
                stored.sizeBytes(),
                stored.contentType(),
                command.durationSeconds());
        videoRepository.save(video);
        return Optional.of(video);
    }

    @Override
    @Transactional
    public Optional<ExerciseVideo> handle(AnalyzeExerciseVideoCommand command) {
        var video = videoRepository.findById(command.videoId())
                .orElseThrow(() -> new IllegalArgumentException("Video not found: " + command.videoId()));
        // Si ya fue analizado, devolvemos el resultado existente (idempotente).
        if (video.isAlreadyAnalyzed()) {
            return Optional.of(video);
        }
        video.markProcessing();
        videoRepository.save(video);
        try {
            byte[] content = cloudStorageService.download(video.getStorageKey());
            ExerciseAnalysisResult result = aiAnalyzer.analyze(video.getExerciseName(), video.getContentType(), content);
            var analysis = new VideoAnalysis(result.summary(), result.overallScore(), result.aiModelVersion());
            for (var item : result.feedback()) {
                analysis.addFeedback(new TechnicalFeedback(
                        item.aspect(), item.message(),
                        parseSeverity(item.severity()), item.timestampSeconds()));
            }
            video.attachAnalysis(analysis);
            videoRepository.save(video);
            return Optional.of(video);
        } catch (RuntimeException e) {
            LOGGER.error("Video analysis failed for video {}: {}", command.videoId(), e.getMessage());
            video.markFailed(e.getMessage());
            videoRepository.save(video);
            return Optional.of(video);
        }
    }

    @Override
    @Transactional
    public void handle(DeleteExerciseVideoCommand command) {
        var video = videoRepository.findById(command.videoId())
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));
        cloudStorageService.delete(video.getStorageKey());
        videoRepository.delete(video);
    }

    private FeedbackSeverity parseSeverity(String severity) {
        if (severity == null) return FeedbackSeverity.INFO;
        try {
            return FeedbackSeverity.valueOf(severity.toUpperCase());
        } catch (IllegalArgumentException e) {
            return FeedbackSeverity.INFO;
        }
    }

    private String resolveVideoMimeType(String declared, String filename) {
        if (declared != null && declared.startsWith("video/")) return declared;
        String ext = "";
        if (filename != null) {
            int dot = filename.lastIndexOf('.');
            if (dot >= 0 && dot < filename.length() - 1) {
                ext = filename.substring(dot + 1).toLowerCase();
            }
        }
        return switch (ext) {
            case "mp4"        -> "video/mp4";
            case "mpeg", "mpg" -> "video/mpeg";
            case "mov"        -> "video/quicktime";
            case "avi"        -> "video/x-msvideo";
            case "flv"        -> "video/x-flv";
            case "webm"       -> "video/webm";
            case "wmv"        -> "video/wmv";
            case "3gp", "3gpp" -> "video/3gpp";
            default            -> "video/mp4";
        };
    }
}
