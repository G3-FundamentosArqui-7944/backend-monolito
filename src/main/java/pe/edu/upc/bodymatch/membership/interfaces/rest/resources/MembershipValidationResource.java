package pe.edu.upc.bodymatch.membership.interfaces.rest.resources;

import java.time.Instant;

public record MembershipValidationResource(
        Long userId,
        boolean active,
        String planCode,
        Instant currentPeriodEnd) {
}
