package pe.edu.upc.bodymatch.membership.domain.model.aggregates;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.InvoiceStatus;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.Money;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.Instant;

@Entity
@NoArgsConstructor
public class Invoice extends AuditableAbstractAggregateRoot<Invoice> {

    @Embedded
    @Getter
    @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Getter
    @Column(nullable = false)
    private Long subscriptionId;

    @Embedded
    @Getter
    @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false))
    @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false))
    private Money amount;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvoiceStatus status;

    @Getter
    @Column(length = 120)
    private String stripeInvoiceId;

    @Getter
    @Column(length = 500)
    private String hostedInvoiceUrl;

    @Getter
    private Instant issuedAt;

    @Getter
    private Instant paidAt;

    public Invoice(UserId userId, Long subscriptionId, Money amount, String stripeInvoiceId, String hostedInvoiceUrl) {
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.stripeInvoiceId = stripeInvoiceId;
        this.hostedInvoiceUrl = hostedInvoiceUrl;
        this.status = InvoiceStatus.OPEN;
        this.issuedAt = Instant.now();
    }

    public void markPaid() {
        this.status = InvoiceStatus.PAID;
        this.paidAt = Instant.now();
    }

    public void markVoid() {
        this.status = InvoiceStatus.VOID;
    }

    public void markUncollectible() {
        this.status = InvoiceStatus.UNCOLLECTIBLE;
    }
}
