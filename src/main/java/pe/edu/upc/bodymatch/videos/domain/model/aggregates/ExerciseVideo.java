package pe.edu.upc.bodymatch.videos.domain.model.aggregates;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.bodymatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.bodymatch.videos.domain.model.entities.VideoAnalysis;
import pe.edu.upc.bodymatch.videos.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.videos.domain.model.valueobjects.VideoProcessingStatus;

@Entity
@NoArgsConstructor
public class ExerciseVideo extends AuditableAbstractAggregateRoot<ExerciseVideo> {

    @Embedded
    @Getter
    @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Getter
    @Column(nullable = false, length = 120)
    private String exerciseName;

    @Getter
    @Column(length = 500)
    private String description;

    @Getter
    @Column(nullable = false, length = 500)
    private String storageKey;

    @Getter
    @Column(nullable = false, length = 1000)
    private String storageUrl;

    @Getter
    @Column(nullable = false)
    private long sizeBytes;

    @Getter
    @Column(length = 50)
    private String contentType;

    @Getter
    @Column
    private Integer durationSeconds;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VideoProcessingStatus status;

    @Getter
    @Column(length = 500)
    private String failureReason;

    @OneToOne(mappedBy = "exerciseVideo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "video_analysis_id")
    @Getter
    private VideoAnalysis analysis;

    public ExerciseVideo(UserId userId, String exerciseName, String description,
                         String storageKey, String storageUrl, long sizeBytes, String contentType, Integer durationSeconds) {
        if (exerciseName == null || exerciseName.isBlank()) {
            throw new IllegalArgumentException("Exercise name required");
        }
        if (storageKey == null || storageKey.isBlank() || storageUrl == null || storageUrl.isBlank()) {
            throw new IllegalArgumentException("Video storage key and URL required");
        }
        if (sizeBytes <= 0) {
            throw new IllegalArgumentException("Video size must be positive");
        }
        this.userId = userId;
        this.exerciseName = exerciseName;
        this.description = description;
        this.storageKey = storageKey;
        this.storageUrl = storageUrl;
        this.sizeBytes = sizeBytes;
        this.contentType = contentType;
        this.durationSeconds = durationSeconds;
        this.status = VideoProcessingStatus.UPLOADED;
    }

    public void markProcessing() {
        // Si ya está analizado, no hacemos nada (idempotente).
        if (status == VideoProcessingStatus.ANALYZED) {
            return;
        }
        this.status = VideoProcessingStatus.PROCESSING;
        this.failureReason = null;
    }

    /** Devuelve true si el video ya tiene un análisis IA exitoso. */
    public boolean isAlreadyAnalyzed() {
        return status == VideoProcessingStatus.ANALYZED && analysis != null;
    }

    public void attachAnalysis(VideoAnalysis analysis) {
        if (analysis == null) throw new IllegalArgumentException("Analysis required");
        analysis.setExerciseVideo(this);
        this.analysis = analysis;
        this.status = VideoProcessingStatus.ANALYZED;
        this.failureReason = null;
    }

    public void markFailed(String reason) {
        this.status = VideoProcessingStatus.FAILED;
        this.failureReason = reason;
    }
}
