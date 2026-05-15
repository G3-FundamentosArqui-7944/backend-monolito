package pe.edu.upc.bodymatch.membership.domain.model.commands;

import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.UserId;

public record ProcessPaymentCommand(
        UserId userId,
        Long subscriptionId,
        String paymentMethodId) {
}
