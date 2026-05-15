package pe.edu.upc.bodymatch.membership.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.membership.domain.model.aggregates.MembershipPlan;
import pe.edu.upc.bodymatch.membership.domain.model.commands.CreateMembershipPlanCommand;
import pe.edu.upc.bodymatch.membership.domain.model.commands.SeedMembershipPlansCommand;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.BillingPeriod;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.Money;
import pe.edu.upc.bodymatch.membership.domain.services.MembershipPlanCommandService;
import pe.edu.upc.bodymatch.membership.infrastructure.persistence.jpa.repositories.MembershipPlanRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class MembershipPlanCommandServiceImpl implements MembershipPlanCommandService {
    private final MembershipPlanRepository planRepository;

    public MembershipPlanCommandServiceImpl(MembershipPlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    @Transactional
    public Optional<MembershipPlan> handle(CreateMembershipPlanCommand command) {
        if (planRepository.existsByCode(command.code())) {
            throw new IllegalArgumentException("Membership plan code already exists: " + command.code());
        }
        var plan = new MembershipPlan(
                command.code(),
                command.name(),
                command.description(),
                new Money(command.priceAmount(), command.currency()),
                command.billingPeriod(),
                command.stripePriceId());
        planRepository.save(plan);
        return Optional.of(plan);
    }

    @Override
    @Transactional
    public void handle(SeedMembershipPlansCommand command) {
        seedIfMissing("BASIC", "Basic Athlete", "Track workouts and basic progression",
                new BigDecimal("9.99"), "USD", BillingPeriod.MONTHLY);
        seedIfMissing("PRO", "Pro Athlete", "Includes AI exercise analysis and nutrition plans",
                new BigDecimal("19.99"), "USD", BillingPeriod.MONTHLY);
        seedIfMissing("ELITE", "Elite Coaching", "Personalized coach matchmaking and unlimited AI feedback",
                new BigDecimal("49.99"), "USD", BillingPeriod.MONTHLY);
    }

    private void seedIfMissing(String code, String name, String description,
                               BigDecimal amount, String currency, BillingPeriod period) {
        if (!planRepository.existsByCode(code)) {
            planRepository.save(new MembershipPlan(code, name, description,
                    new Money(amount, currency), period, null));
        }
    }
}
