package pe.edu.upc.bodymatch.membership.interfaces.rest.transform;

import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Payment;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.PaymentResource;

public class PaymentResourceFromEntityAssembler {
    public static PaymentResource toResourceFromEntity(Payment payment) {
        return new PaymentResource(
                payment.getId(),
                payment.getUserId().userId(),
                payment.getSubscriptionId(),
                payment.getAmount().amount(),
                payment.getAmount().currency(),
                payment.getStatus().name(),
                payment.getStripePaymentIntentId(),
                payment.getDescription(),
                payment.getProcessedAt(),
                payment.getFailureReason());
    }
}
