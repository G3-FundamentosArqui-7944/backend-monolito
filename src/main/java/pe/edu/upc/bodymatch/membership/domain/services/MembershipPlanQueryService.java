package pe.edu.upc.bodymatch.membership.domain.services;

import pe.edu.upc.bodymatch.membership.domain.model.aggregates.MembershipPlan;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetAllMembershipPlansQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetMembershipPlanByCodeQuery;

import java.util.List;
import java.util.Optional;

public interface MembershipPlanQueryService {
    List<MembershipPlan> handle(GetAllMembershipPlansQuery query);
    Optional<MembershipPlan> handle(GetMembershipPlanByCodeQuery query);
}
