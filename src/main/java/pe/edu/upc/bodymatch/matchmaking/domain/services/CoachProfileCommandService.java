package pe.edu.upc.bodymatch.matchmaking.domain.services;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.CoachProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.AddCoachAvailabilityCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CreateCoachProfileCommand;

import java.util.Optional;

public interface CoachProfileCommandService {
    Optional<CoachProfile> handle(CreateCoachProfileCommand command);
    Optional<CoachProfile> handle(AddCoachAvailabilityCommand command);
}
