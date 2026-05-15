package pe.edu.upc.bodymatch.matchmaking.domain.services;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.ConnectionRequest;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.RequestCoachingCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.RespondConnectionRequestCommand;

import java.util.Optional;

public interface ConnectionRequestCommandService {
    Optional<ConnectionRequest> handle(RequestCoachingCommand command);
    Optional<ConnectionRequest> handle(RespondConnectionRequestCommand command);
}
