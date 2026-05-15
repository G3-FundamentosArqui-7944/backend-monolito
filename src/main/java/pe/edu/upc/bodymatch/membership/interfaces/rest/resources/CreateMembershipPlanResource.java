package pe.edu.upc.bodymatch.membership.interfaces.rest.resources;

import java.math.BigDecimal;

public record CreateMembershipPlanResource(
        String code,
        String name,
        String description,
        BigDecimal priceAmount,
        String currency,
        String billingPeriod,
        String stripePriceId) {
}
