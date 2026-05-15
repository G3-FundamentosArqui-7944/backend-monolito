package pe.edu.upc.bodymatch.membership.interfaces.rest.transform;

import pe.edu.upc.bodymatch.membership.domain.model.aggregates.MembershipPlan;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.MembershipPlanResource;

public class MembershipPlanResourceFromEntityAssembler {
    public static MembershipPlanResource toResourceFromEntity(MembershipPlan plan) {
        return new MembershipPlanResource(
                plan.getId(),
                plan.getCode(),
                plan.getName(),
                plan.getDescription(),
                plan.getPrice().amount(),
                plan.getPrice().currency(),
                plan.getBillingPeriod().name(),
                plan.isActive(),
                plan.getStripePriceId());
    }
}
