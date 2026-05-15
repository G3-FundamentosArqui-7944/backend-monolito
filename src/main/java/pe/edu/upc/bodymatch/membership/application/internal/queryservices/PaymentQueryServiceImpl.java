package pe.edu.upc.bodymatch.membership.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Invoice;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Payment;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetInvoicesByUserIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetPaymentsByUserIdQuery;
import pe.edu.upc.bodymatch.membership.domain.services.PaymentQueryService;
import pe.edu.upc.bodymatch.membership.infrastructure.persistence.jpa.repositories.InvoiceRepository;
import pe.edu.upc.bodymatch.membership.infrastructure.persistence.jpa.repositories.PaymentRepository;

import java.util.List;

@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<Payment> handle(GetPaymentsByUserIdQuery query) {
        return paymentRepository.findAllByUserIdOrderByCreatedAtDesc(query.userId());
    }

    @Override
    public List<Invoice> handle(GetInvoicesByUserIdQuery query) {
        return invoiceRepository.findAllByUserIdOrderByIssuedAtDesc(query.userId());
    }
}
