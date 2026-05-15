package pe.edu.upc.bodymatch.nutrition.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.MealRecord;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetDailyMacroSummaryQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.queries.GetMealsByUserQuery;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.MacroSummary;
import pe.edu.upc.bodymatch.nutrition.domain.services.MealRecordQueryService;
import pe.edu.upc.bodymatch.nutrition.infrastructure.persistence.jpa.repositories.MealRecordRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class MealRecordQueryServiceImpl implements MealRecordQueryService {
    private final MealRecordRepository mealRecordRepository;

    public MealRecordQueryServiceImpl(MealRecordRepository mealRecordRepository) {
        this.mealRecordRepository = mealRecordRepository;
    }

    @Override
    public List<MealRecord> handle(GetMealsByUserQuery query) {
        if (query.from() != null && query.to() != null) {
            return mealRecordRepository.findAllByUserIdAndConsumedAtBetweenOrderByConsumedAtDesc(
                    query.userId(), query.from(), query.to());
        }
        return mealRecordRepository.findAllByUserIdOrderByConsumedAtDesc(query.userId());
    }

    @Override
    public MacroSummary handle(GetDailyMacroSummaryQuery query) {
        LocalDate date = query.date() != null ? query.date() : LocalDate.now();
        Instant from = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        var meals = mealRecordRepository.findAllByUserIdAndConsumedAtBetweenOrderByConsumedAtDesc(query.userId(), from, to);
        MacroSummary total = new MacroSummary();
        for (var meal : meals) {
            total = total.add(meal.getMacros());
        }
        return total;
    }
}
