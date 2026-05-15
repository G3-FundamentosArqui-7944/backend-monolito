package pe.edu.upc.bodymatch.iam.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.bodymatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.Instant;
import java.util.UUID;

@Entity
public class RefreshToken extends AuditableAbstractAggregateRoot<RefreshToken> {

    @Getter
    @Column(nullable = false, unique = true, length = 80)
    private String token;

    @Getter
    @Column(nullable = false)
    private Long userId;

    @Getter
    @Column(nullable = false)
    private Instant expiresAt;

    @Getter
    @Column(nullable = false)
    private boolean revoked;

    public RefreshToken() {}

    public RefreshToken(Long userId, Instant expiresAt) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User id must be positive");
        }
        if (expiresAt == null || expiresAt.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Refresh token expiration must be in the future");
        }
        this.token = UUID.randomUUID().toString();
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.revoked = false;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isUsable() {
        return !revoked && !isExpired();
    }

    public void revoke() {
        this.revoked = true;
    }
}
