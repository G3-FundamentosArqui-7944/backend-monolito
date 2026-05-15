package pe.edu.upc.bodymatch.training.domain.model.commands;

import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;

import java.time.Instant;

public record RegisterProgressMilestoneCommand(UserId userId, String milestone, String description, Instant achievedAt) {
}
