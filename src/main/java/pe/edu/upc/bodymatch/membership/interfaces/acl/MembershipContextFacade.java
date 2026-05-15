package pe.edu.upc.bodymatch.membership.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.membership.domain.services.SubscriptionQueryService;

import java.util.Optional;

@Service
public class MembershipContextFacade {
    private final SubscriptionQueryService subscriptionQueryService;

    public MembershipContextFacade(SubscriptionQueryService subscriptionQueryService) {
        this.subscriptionQueryService = subscriptionQueryService;
    }

    public boolean hasActiveMembership(Long userId) {
        var subscription = subscriptionQueryService.handle(new GetActiveSubscriptionByUserIdQuery(new UserId(userId)));
        return subscription.isPresent() && subscription.get().isCurrentlyActive();
    }

    public Optional<String> fetchActivePlanCode(Long userId) {
        return subscriptionQueryService.handle(new GetActiveSubscriptionByUserIdQuery(new UserId(userId)))
                .filter(s -> s.isCurrentlyActive())
                .map(s -> s.getPlan().getCode());
    }
}
