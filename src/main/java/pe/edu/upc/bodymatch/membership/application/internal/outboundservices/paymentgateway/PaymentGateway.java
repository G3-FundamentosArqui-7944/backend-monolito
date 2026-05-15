package pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway;

import pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto.GatewayChargeRequest;
import pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto.GatewayChargeResult;
import pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto.GatewayCustomerResult;
import pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto.GatewaySubscriptionResult;
import pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto.GatewayWebhookEvent;

public interface PaymentGateway {
    GatewayCustomerResult createCustomer(String email, String fullName);
    GatewaySubscriptionResult createSubscription(String customerId, String priceId);
    void cancelSubscription(String subscriptionId);
    GatewayChargeResult chargePayment(GatewayChargeRequest request);
    GatewayWebhookEvent parseWebhookEvent(String payload, String signatureHeader);
}
