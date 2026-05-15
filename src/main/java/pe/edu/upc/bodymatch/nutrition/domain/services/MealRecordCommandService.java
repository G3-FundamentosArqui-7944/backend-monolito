package pe.edu.upc.bodymatch.nutrition.domain.services;

import pe.edu.upc.bodymatch.nutrition.domain.model.aggregates.MealRecord;
import pe.edu.upc.bodymatch.nutrition.domain.model.commands.LogMealCommand;

import java.util.Optional;

public interface MealRecordCommandService {
    Optional<MealRecord> handle(LogMealCommand command);
}
