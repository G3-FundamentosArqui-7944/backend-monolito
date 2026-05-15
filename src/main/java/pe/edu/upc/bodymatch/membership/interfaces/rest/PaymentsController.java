package pe.edu.upc.bodymatch.membership.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.membership.domain.model.commands.HandleStripeWebhookCommand;
import pe.edu.upc.bodymatch.membership.domain.model.commands.ProcessPaymentCommand;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetInvoicesByUserIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetPaymentsByUserIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.membership.domain.services.PaymentQueryService;
import pe.edu.upc.bodymatch.membership.domain.services.SubscriptionCommandService;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.InvoiceResource;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.PaymentResource;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.ProcessPaymentResource;
import pe.edu.upc.bodymatch.membership.interfaces.rest.transform.InvoiceResourceFromEntityAssembler;
import pe.edu.upc.bodymatch.membership.interfaces.rest.transform.PaymentResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/payments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Payments", description = "Payment processing and history")
public class PaymentsController {
    private final SubscriptionCommandService subscriptionCommandService;
    private final PaymentQueryService paymentQueryService;

    public PaymentsController(SubscriptionCommandService subscriptionCommandService,
                              PaymentQueryService paymentQueryService) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.paymentQueryService = paymentQueryService;
    }

    @PostMapping("/charge")
    public ResponseEntity<PaymentResource> processPayment(@RequestBody ProcessPaymentResource resource) {
        try {
            var command = new ProcessPaymentCommand(
                    new UserId(resource.userId()),
                    resource.subscriptionId(),
                    resource.paymentMethodId());
            var payment = subscriptionCommandService.handle(command);
            if (payment.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    PaymentResourceFromEntityAssembler.toResourceFromEntity(payment.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResource>> getPaymentsByUser(@PathVariable Long userId) {
        var payments = paymentQueryService.handle(new GetPaymentsByUserIdQuery(new UserId(userId)));
        var resources = payments.stream()
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/user/{userId}/invoices")
    public ResponseEntity<List<InvoiceResource>> getInvoicesByUser(@PathVariable Long userId) {
        var invoices = paymentQueryService.handle(new GetInvoicesByUserIdQuery(new UserId(userId)));
        var resources = invoices.stream()
                .map(InvoiceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/webhook/stripe")
    public ResponseEntity<Void> stripeWebhook(@RequestBody String payload,
                                              @RequestHeader("Stripe-Signature") String signature) {
        try {
            subscriptionCommandService.handle(new HandleStripeWebhookCommand(payload, signature));
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
