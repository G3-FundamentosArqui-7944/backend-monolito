package pe.edu.upc.bodymatch.membership.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Payment;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByUserIdOrderByCreatedAtDesc(UserId userId);
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
    List<Payment> findAllBySubscriptionId(Long subscriptionId);
}
