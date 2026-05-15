package pe.edu.upc.bodymatch.membership.application.internal.commandservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.membership.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.PaymentGateway;
import pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto.GatewayChargeRequest;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Payment;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Subscription;
import pe.edu.upc.bodymatch.membership.domain.model.commands.CancelSubscriptionCommand;
import pe.edu.upc.bodymatch.membership.domain.model.commands.CreateSubscriptionCommand;
import pe.edu.upc.bodymatch.membership.domain.model.commands.HandleStripeWebhookCommand;
import pe.edu.upc.bodymatch.membership.domain.model.commands.ProcessPaymentCommand;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.SubscriptionStatus;
import pe.edu.upc.bodymatch.membership.domain.services.SubscriptionCommandService;
import pe.edu.upc.bodymatch.membership.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;
import pe.edu.upc.bodymatch.membership.infrastructure.persistence.jpa.repositories.PaymentRepository;
import pe.edu.upc.bodymatch.membership.infrastructure.persistence.jpa.repositories.SubscriptionRepository;

import java.util.Optional;

@Service
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionCommandServiceImpl.class);

    private final SubscriptionRepository subscriptionRepository;
    private final MembershipPlanRepository planRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;
    private final ExternalIamService externalIamService;

    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository,
                                          MembershipPlanRepository planRepository,
                                          PaymentRepository paymentRepository,
                                          PaymentGateway paymentGateway,
                                          ExternalIamService externalIamService) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public Optional<Subscription> handle(CreateSubscriptionCommand command) {
        if (!externalIamService.existsUserById(command.userId())) {
            throw new IllegalArgumentException("User does not exist: " + command.userId().userId());
        }
        var existingActive = subscriptionRepository.findByUserIdAndStatus(command.userId(), SubscriptionStatus.ACTIVE);
        if (existingActive.isPresent()) {
            throw new IllegalStateException("User already has an active subscription");
        }
        var plan = planRepository.findByCode(command.planCode())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + command.planCode()));
        if (!plan.isActive()) {
            throw new IllegalStateException("Plan is not active: " + command.planCode());
        }

        String customerId = null;
        if (plan.getStripePriceId() != null && !plan.getStripePriceId().isBlank()) {
            try {
                var email = externalIamService.fetchEmailByUserId(command.userId());
                customerId = paymentGateway.createCustomer(email, email).customerId();
            } catch (RuntimeException e) {
                LOGGER.warn("Stripe customer creation skipped: {}", e.getMessage());
            }
        }

        var subscription = new Subscription(command.userId(), plan, customerId);
        subscriptionRepository.save(subscription);
        return Optional.of(subscription);
    }

    @Override
    @Transactional
    public Optional<Subscription> handle(CancelSubscriptionCommand command) {
        var subscription = subscriptionRepository.findById(command.subscriptionId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + command.subscriptionId()));
        if (subscription.getStripeSubscriptionId() != null) {
            try {
                paymentGateway.cancelSubscription(subscription.getStripeSubscriptionId());
            } catch (RuntimeException e) {
                LOGGER.warn("Stripe cancel failed (continuing): {}", e.getMessage());
            }
        }
        subscription.cancel();
        subscriptionRepository.save(subscription);
        return Optional.of(subscription);
    }

    @Override
    @Transactional
    public Optional<Payment> handle(ProcessPaymentCommand command) {
        var subscription = subscriptionRepository.findById(command.subscriptionId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + command.subscriptionId()));
        if (!subscription.getUserId().equals(command.userId())) {
            throw new IllegalArgumentException("Subscription does not belong to user");
        }
        var plan = subscription.getPlan();
        var payment = new Payment(command.userId(), subscription.getId(), plan.getPrice(),
                "Charge for plan " + plan.getCode());
        paymentRepository.save(payment);

        if (subscription.getStripeCustomerId() == null || subscription.getStripeCustomerId().isBlank()) {
            payment.markFailed("Subscription has no Stripe customer linked");
            paymentRepository.save(payment);
            return Optional.of(payment);
        }

        var chargeRequest = new GatewayChargeRequest(
                subscription.getStripeCustomerId(),
                plan.getPrice().toMinorUnits(),
                plan.getPrice().currency(),
                command.paymentMethodId(),
                "BodyMatch AI plan " + plan.getCode());
        var result = paymentGateway.chargePayment(chargeRequest);
        if (result.succeeded()) {
            payment.markSucceeded(result.paymentIntentId());
            subscription.activate(subscription.getStripeSubscriptionId());
            subscriptionRepository.save(subscription);
        } else {
            payment.markFailed(result.failureMessage());
        }
        paymentRepository.save(payment);
        return Optional.of(payment);
    }

    @Override
    @Transactional
    public void handle(HandleStripeWebhookCommand command) {
        var event = paymentGateway.parseWebhookEvent(command.payload(), command.signature());
        LOGGER.info("Received Stripe webhook event: {}", event.type());

        switch (event.type()) {
            case "invoice.payment_succeeded" -> handleInvoicePaid(event);
            case "invoice.payment_failed" -> handleInvoiceFailed(event);
            case "customer.subscription.deleted" -> handleSubscriptionDeleted(event);
            default -> LOGGER.debug("Ignoring webhook event type: {}", event.type());
        }
    }

    private void handleInvoicePaid(pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto.GatewayWebhookEvent event) {
        var subscriptionId = event.data().get("subscription");
        if (subscriptionId == null) return;
        subscriptionRepository.findByStripeSubscriptionId(subscriptionId).ifPresent(s -> {
            s.renew();
            subscriptionRepository.save(s);
        });
    }

    private void handleInvoiceFailed(pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto.GatewayWebhookEvent event) {
        var subscriptionId = event.data().get("subscription");
        if (subscriptionId == null) return;
        subscriptionRepository.findByStripeSubscriptionId(subscriptionId).ifPresent(s -> {
            s.markPastDue();
            subscriptionRepository.save(s);
        });
    }

    private void handleSubscriptionDeleted(pe.edu.upc.bodymatch.membership.application.internal.outboundservices.paymentgateway.dto.GatewayWebhookEvent event) {
        var subscriptionId = event.data().get("id");
        if (subscriptionId == null) return;
        subscriptionRepository.findByStripeSubscriptionId(subscriptionId).ifPresent(s -> {
            s.cancel();
            subscriptionRepository.save(s);
        });
    }
}
