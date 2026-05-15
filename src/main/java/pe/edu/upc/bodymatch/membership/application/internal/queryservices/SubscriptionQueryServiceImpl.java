package pe.edu.upc.bodymatch.membership.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Subscription;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetSubscriptionByIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetSubscriptionsByUserIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.SubscriptionStatus;
import pe.edu.upc.bodymatch.membership.domain.services.SubscriptionQueryService;
import pe.edu.upc.bodymatch.membership.infrastructure.persistence.jpa.repositories.SubscriptionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionByIdQuery query) {
        return subscriptionRepository.findById(query.subscriptionId());
    }

    @Override
    public List<Subscription> handle(GetSubscriptionsByUserIdQuery query) {
        return subscriptionRepository.findAllByUserId(query.userId());
    }

    @Override
    public Optional<Subscription> handle(GetActiveSubscriptionByUserIdQuery query) {
        return subscriptionRepository.findByUserIdAndStatus(query.userId(), SubscriptionStatus.ACTIVE);
    }
}
