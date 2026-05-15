package pe.edu.upc.bodymatch.membership.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResource(
        Long id,
        Long userId,
        Long subscriptionId,
        BigDecimal amount,
        String currency,
        String status,
        String stripePaymentIntentId,
        String description,
        Instant processedAt,
        String failureReason) {
}
