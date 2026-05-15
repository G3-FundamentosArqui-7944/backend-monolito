package pe.edu.upc.bodymatch.membership.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.membership.domain.model.commands.CancelSubscriptionCommand;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetSubscriptionByIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetSubscriptionsByUserIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.membership.domain.services.SubscriptionCommandService;
import pe.edu.upc.bodymatch.membership.domain.services.SubscriptionQueryService;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.CreateSubscriptionResource;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.MembershipValidationResource;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.SubscriptionResource;
import pe.edu.upc.bodymatch.membership.interfaces.rest.transform.CreateSubscriptionCommandFromResourceAssembler;
import pe.edu.upc.bodymatch.membership.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Subscriptions", description = "Subscription lifecycle endpoints")
public class SubscriptionsController {
    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;

    public SubscriptionsController(SubscriptionCommandService subscriptionCommandService,
                                   SubscriptionQueryService subscriptionQueryService) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResource> create(@RequestBody CreateSubscriptionResource resource) {
        try {
            var command = CreateSubscriptionCommandFromResourceAssembler.toCommandFromResource(resource);
            var subscription = subscriptionCommandService.handle(command);
            if (subscription.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResource> cancel(@PathVariable Long subscriptionId) {
        try {
            var result = subscriptionCommandService.handle(new CancelSubscriptionCommand(subscriptionId));
            if (result.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(SubscriptionResourceFromEntityAssembler.toResourceFromEntity(result.get()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResource> getById(@PathVariable Long subscriptionId) {
        var subscription = subscriptionQueryService.handle(new GetSubscriptionByIdQuery(subscriptionId));
        if (subscription.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionResource>> getByUser(@PathVariable Long userId) {
        var subscriptions = subscriptionQueryService.handle(new GetSubscriptionsByUserIdQuery(new UserId(userId)));
        var resources = subscriptions.stream()
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/user/{userId}/membership-status")
    public ResponseEntity<MembershipValidationResource> validateMembership(@PathVariable Long userId) {
        var active = subscriptionQueryService.handle(new GetActiveSubscriptionByUserIdQuery(new UserId(userId)));
        if (active.isEmpty() || !active.get().isCurrentlyActive()) {
            return ResponseEntity.ok(new MembershipValidationResource(userId, false, null, null));
        }
        var s = active.get();
        return ResponseEntity.ok(new MembershipValidationResource(userId, true, s.getPlan().getCode(), s.getCurrentPeriodEnd()));
    }
}
