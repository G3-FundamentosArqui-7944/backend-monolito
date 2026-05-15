package pe.edu.upc.bodymatch.nutrition.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.nutrition.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.MealRecord;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.LogMealCommand;
import pe.edu.upc.bodymatch.nutrition.domain.services.MealRecordCommandService;
import pe.edu.upc.bodymatch.nutrition.infrastructure.persistence.jpa.repositories.MealRecordRepository;

import java.util.Optional;

@Service
public class MealRecordCommandServiceImpl implements MealRecordCommandService {
    private final MealRecordRepository mealRecordRepository;
    private final ExternalIamService externalIamService;

    public MealRecordCommandServiceImpl(MealRecordRepository mealRecordRepository, ExternalIamService externalIamService) {
        this.mealRecordRepository = mealRecordRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public Optional<MealRecord> handle(LogMealCommand command) {
        if (!externalIamService.existsUser(command.userId())) {
            throw new IllegalArgumentException("User does not exist: " + command.userId().userId());
        }
        var record = new MealRecord(
                command.userId(),
                command.mealType(),
                command.description(),
                command.macros(),
                command.consumedAt(),
                command.sourceAnalysisId());
        mealRecordRepository.save(record);
        return Optional.of(record);
    }
}
