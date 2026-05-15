package pe.edu.upc.bodymatch.membership.domain.services;

import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Subscription;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetSubscriptionByIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetSubscriptionsByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryService {
    Optional<Subscription> handle(GetSubscriptionByIdQuery query);
    List<Subscription> handle(GetSubscriptionsByUserIdQuery query);
    Optional<Subscription> handle(GetActiveSubscriptionByUserIdQuery query);
}
