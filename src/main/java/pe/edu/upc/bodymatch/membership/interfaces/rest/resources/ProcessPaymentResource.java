package pe.edu.upc.bodymatch.membership.interfaces.rest.resources;

public record ProcessPaymentResource(Long userId, Long subscriptionId, String paymentMethodId) {
}
