package pe.edu.upc.bodymatch.membership.domain.services;

import pe.edu.upc.bodymatch.membership.domain.model.aggregates.MembershipPlan;
import pe.edu.upc.bodymatch.membership.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.bodymatch.membership.domain.model.commands.SeedMembershipPlansCommand;

import java.util.Optional;

public interface MembershipPlanCommandService {
    Optional<MembershipPlan> handle(CreateMembershipPlanCommand command);
    void handle(SeedMembershipPlansCommand command);
}
