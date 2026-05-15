package pe.edu.upc.bodymatch.membership.interfaces.rest.transform;

import pe.edu.upc.bodymatch.membership.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.BillingPeriod;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.CreateMembershipPlanResource;

public class CreateMembershipPlanCommandFromResourceAssembler {
    public static CreateMembershipPlanCommand toCommandFromResource(CreateMembershipPlanResource resource) {
        return new CreateMembershipPlanCommand(
                resource.code(),
                resource.name(),
                resource.description(),
                resource.priceAmount(),
                resource.currency(),
                BillingPeriod.valueOf(resource.billingPeriod()),
                resource.stripePriceId());
    }
}
