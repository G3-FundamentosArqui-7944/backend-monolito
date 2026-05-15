package pe.edu.upc.bodymatch.membership.interfaces.rest.resources;

import java.math.BigDecimal;

public record MembershipPlanResource(
        Long id,
        String code,
        String name,
        String description,
        BigDecimal priceAmount,
        String currency,
        String billingPeriod,
        boolean active,
        String stripePriceId) {
}
