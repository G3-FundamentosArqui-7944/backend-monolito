package pe.edu.upc.bodymatch.matchmaking.domain.services;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.AthleteProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CreateAthleteProfileCommand;

import java.util.Optional;

public interface AthleteProfileCommandService {
    Optional<AthleteProfile> handle(CreateAthleteProfileCommand command);
}
