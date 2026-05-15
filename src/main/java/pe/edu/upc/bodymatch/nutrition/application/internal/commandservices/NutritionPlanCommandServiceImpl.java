package pe.edu.upc.bodymatch.nutrition.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.nutrition.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.NutritionPlan;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.CreateNutritionPlanCommand;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.DeactivateNutritionPlanCommand;
import pe.edu.upc.bodymatch.nutrition.domain.services.NutritionPlanCommandService;
import pe.edu.upc.bodymatch.nutrition.infrastructure.persistence.jpa.repositories.NutritionPlanRepository;

import java.util.Optional;

@Service
public class NutritionPlanCommandServiceImpl implements NutritionPlanCommandService {

    private final NutritionPlanRepository nutritionPlanRepository;
    private final ExternalIamService externalIamService;

    public NutritionPlanCommandServiceImpl(NutritionPlanRepository nutritionPlanRepository,
                                           ExternalIamService externalIamService) {
        this.nutritionPlanRepository = nutritionPlanRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public Optional<NutritionPlan> handle(CreateNutritionPlanCommand command) {
        if (!externalIamService.existsUser(command.userId())) {
            throw new IllegalArgumentException("User does not exist: " + command.userId().userId());
        }
        nutritionPlanRepository.findByUserIdAndActiveTrue(command.userId()).ifPresent(existing -> {
            existing.deactivate();
            nutritionPlanRepository.save(existing);
        });
        var plan = new NutritionPlan(command.userId(), command.name(), command.description(),
                command.dailyTargets(), command.startDate(), command.endDate());
        nutritionPlanRepository.save(plan);
        return Optional.of(plan);
    }

    @Override
    @Transactional
    public Optional<NutritionPlan> handle(DeactivateNutritionPlanCommand command) {
        var plan = nutritionPlanRepository.findById(command.planId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        plan.deactivate();
        nutritionPlanRepository.save(plan);
        return Optional.of(plan);
    }
}
