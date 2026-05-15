package pe.edu.upc.bodymatch.membership.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.MembershipPlan;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetAllMembershipPlansQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetMembershipPlanByCodeQuery;
import pe.edu.upc.bodymatch.membership.domain.services.MembershipPlanQueryService;
import pe.edu.upc.bodymatch.membership.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MembershipPlanQueryServiceImpl implements MembershipPlanQueryService {
    private final MembershipPlanRepository planRepository;

    public MembershipPlanQueryServiceImpl(MembershipPlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<MembershipPlan> handle(GetAllMembershipPlansQuery query) {
        return query.onlyActive() ? planRepository.findAllByActiveTrue() : planRepository.findAll();
    }

    @Override
    public Optional<MembershipPlan> handle(GetMembershipPlanByCodeQuery query) {
        return planRepository.findByCode(query.code());
    }
}
