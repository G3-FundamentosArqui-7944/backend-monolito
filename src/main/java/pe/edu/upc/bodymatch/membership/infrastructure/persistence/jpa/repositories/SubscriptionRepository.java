package pe.edu.upc.bodymatch.membership.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Subscription;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.SubscriptionStatus;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUserId(UserId userId);
    Optional<Subscription> findByUserIdAndStatus(UserId userId, SubscriptionStatus status);
    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);
}
