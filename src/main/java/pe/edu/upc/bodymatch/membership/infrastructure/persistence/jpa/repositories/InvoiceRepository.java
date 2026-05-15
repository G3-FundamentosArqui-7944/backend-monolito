package pe.edu.upc.bodymatch.membership.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Invoice;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByUserIdOrderByIssuedAtDesc(UserId userId);
    Optional<Invoice> findByStripeInvoiceId(String stripeInvoiceId);
}
