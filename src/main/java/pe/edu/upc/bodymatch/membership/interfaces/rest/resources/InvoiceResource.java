package pe.edu.upc.bodymatch.membership.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record InvoiceResource(
        Long id,
        Long userId,
        Long subscriptionId,
        BigDecimal amount,
        String currency,
        String status,
        String stripeInvoiceId,
        String hostedInvoiceUrl,
        Instant issuedAt,
        Instant paidAt) {
}
