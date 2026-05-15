package pe.edu.upc.bodymatch.videos.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.bodymatch.shared.domain.model.entities.AuditableModel;
import pe.edu.upc.bodymatch.videos.domain.model.valueobjects.FeedbackSeverity;

@Entity
@NoArgsConstructor
public class TechnicalFeedback extends AuditableModel {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false, length = 200)
    private String aspect;

    @Getter
    @Column(nullable = false, length = 2000)
    private String message;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FeedbackSeverity severity;

    @Getter
    @Column
    private Integer timestampSeconds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_analysis_id")
    @Getter
    @Setter
    private VideoAnalysis videoAnalysis;

    public TechnicalFeedback(String aspect, String message, FeedbackSeverity severity, Integer timestampSeconds) {
        if (aspect == null || aspect.isBlank()) throw new IllegalArgumentException("Aspect required");
        if (message == null || message.isBlank()) throw new IllegalArgumentException("Message required");
        this.aspect = aspect;
        this.message = message;
        this.severity = severity == null ? FeedbackSeverity.INFO : severity;
        this.timestampSeconds = timestampSeconds;
    }
}
