package pe.edu.upc.bodymatch.membership.domain.model.commands;

import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.BillingPeriod;

import java.math.BigDecimal;

public record CreateMembershipPlanCommand(
        String code,
        String name,
        String description,
        BigDecimal priceAmount,
        String currency,
        BillingPeriod billingPeriod,
        String stripePriceId) {
}
