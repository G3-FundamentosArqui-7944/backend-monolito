package pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto;

import java.util.Map;

public record GatewayWebhookEvent(String type, Map<String, String> data) {
}
