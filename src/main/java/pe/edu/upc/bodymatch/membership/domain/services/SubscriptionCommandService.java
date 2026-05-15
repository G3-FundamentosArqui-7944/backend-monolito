package pe.edu.upc.bodymatch.membership.domain.services;

import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Payment;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Subscription;
import pe.edu.upc.bodymatch.membership.domain.model.commands.CancelSubscriptionCommand;
import pe.edu.upc.bodymatch.membership.domain.model.commands.CreateSubscriptionCommand;
import pe.edu.upc.bodymatch.membership.domain.model.commands.HandleStripeWebhookCommand;
import pe.edu.upc.bodymatch.membership.domain.model.commands.ProcessPaymentCommand;

import java.util.Optional;

public interface SubscriptionCommandService {
    Optional<Subscription> handle(CreateSubscriptionCommand command);
    Optional<Subscription> handle(CancelSubscriptionCommand command);
    Optional<Payment> handle(ProcessPaymentCommand command);
    void handle(HandleStripeWebhookCommand command);
}
