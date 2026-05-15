package pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto;

public record GatewaySubscriptionResult(String subscriptionId, String status, String latestInvoiceId) {
}
