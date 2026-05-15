package pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto;

public record GatewayChargeRequest(
        String customerId,
        long amountInMinorUnits,
        String currency,
        String paymentMethodId,
        String description) {
}
