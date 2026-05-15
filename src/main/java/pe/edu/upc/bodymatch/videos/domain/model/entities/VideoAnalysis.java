package pe.edu.upc.bodymatch.videos.domain.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.bodymatch.shared.domain.model.entities.AuditableModel;
import pe.edu.upc.bodymatch.videos.domain.model.aggregates.ExerciseVideo;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class VideoAnalysis extends AuditableModel {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false, length = 4000)
    private String summary;

    @Getter
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal overallScore;

    @Getter
    @Column(nullable = false, length = 80)
    private String aiModelVersion;

    @Getter
    @Column(nullable = false)
    private Instant analyzedAt;

    @OneToMany(mappedBy = "videoAnalysis", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter
    private List<TechnicalFeedback> feedbackItems;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_video_id")
    @Getter
    @Setter
    private ExerciseVideo exerciseVideo;

    public VideoAnalysis(String summary, BigDecimal overallScore, String aiModelVersion) {
        if (summary == null || summary.isBlank()) {
            throw new IllegalArgumentException("Analysis summary is required");
        }
        if (overallScore == null || overallScore.compareTo(BigDecimal.ZERO) < 0 || overallScore.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Overall score must be between 0 and 100");
        }
        this.summary = summary;
        this.overallScore = overallScore;
        this.aiModelVersion = aiModelVersion == null ? "unknown" : aiModelVersion;
        this.analyzedAt = Instant.now();
        this.feedbackItems = new ArrayList<>();
    }

    public void addFeedback(TechnicalFeedback feedback) {
        feedback.setVideoAnalysis(this);
        this.feedbackItems.add(feedback);
    }
}
