package pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto;

public record GatewayChargeResult(
        String paymentIntentId,
        String status,
        String failureMessage) {

    public boolean succeeded() {
        return "succeeded".equalsIgnoreCase(status);
    }
}
