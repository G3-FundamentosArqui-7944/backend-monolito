package pe.edu.upc.bodymatch.membership.domain.model.commands;

public record HandleStripeWebhookCommand(String payload, String signature) {
}
