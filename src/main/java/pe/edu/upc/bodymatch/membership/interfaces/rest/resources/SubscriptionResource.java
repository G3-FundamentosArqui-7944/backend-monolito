package pe.edu.upc.bodymatch.membership.interfaces.rest.resources;

import java.time.Instant;

public record SubscriptionResource(
        Long id,
        Long userId,
        String planCode,
        String status,
        Instant startDate,
        Instant currentPeriodEnd,
        Instant cancelledAt,
        String stripeSubscriptionId) {
}
