package pe.edu.upc.bodymatch.membership.interfaces.rest.transform;

import pe.edu.upc.bodymatch.membership.domain.model.aggregates.Invoice;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.InvoiceResource;

public class InvoiceResourceFromEntityAssembler {
    public static InvoiceResource toResourceFromEntity(Invoice invoice) {
        return new InvoiceResource(
                invoice.getId(),
                invoice.getUserId().userId(),
                invoice.getSubscriptionId(),
                invoice.getAmount().amount(),
                invoice.getAmount().currency(),
                invoice.getStatus().name(),
                invoice.getStripeInvoiceId(),
                invoice.getHostedInvoiceUrl(),
                invoice.getIssuedAt(),
                invoice.getPaidAt());
    }
}
