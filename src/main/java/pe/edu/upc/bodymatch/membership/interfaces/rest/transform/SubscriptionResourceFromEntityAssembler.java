package pe.edu.upc.bodymatch.membership.interfaces.rest.transform;

import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Subscription;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.SubscriptionResource;

public class SubscriptionResourceFromEntityAssembler {
    public static SubscriptionResource toResourceFromEntity(Subscription subscription) {
        return new SubscriptionResource(
                subscription.getId(),
                subscription.getUserId().userId(),
                subscription.getPlan().getCode(),
                subscription.getStatus().name(),
                subscription.getStartDate(),
                subscription.getCurrentPeriodEnd(),
                subscription.getCancelledAt(),
                subscription.getStripeSubscriptionId());
    }
}
