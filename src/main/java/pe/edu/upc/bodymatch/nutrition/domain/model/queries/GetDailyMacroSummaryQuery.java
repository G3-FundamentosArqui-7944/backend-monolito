package pe.edu.upc.bodymatch.nutrition.domain.model.queries;

import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;

import java.time.LocalDate;

public record GetDailyMacroSummaryQuery(UserId userId, LocalDate date) {
}
