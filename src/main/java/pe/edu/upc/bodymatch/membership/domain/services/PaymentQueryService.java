package pe.edu.upc.bodymatch.membership.domain.services;

import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Invoice;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Payment;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetInvoicesByUserIdQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetPaymentsByUserIdQuery;

import java.util.List;

public interface PaymentQueryService {
    List<Payment> handle(GetPaymentsByUserIdQuery query);
    List<Invoice> handle(GetInvoicesByUserIdQuery query);
}
