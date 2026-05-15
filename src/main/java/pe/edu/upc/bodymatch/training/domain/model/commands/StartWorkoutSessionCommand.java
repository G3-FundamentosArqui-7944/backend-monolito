package pe.edu.upc.bodymatch.training.domain.model.commands;

import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;

import java.time.Instant;

public record StartWorkoutSessionCommand(UserId userId, String title, Instant startedAt, String notes) {
}
