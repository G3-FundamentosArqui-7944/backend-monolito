package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform;

import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.ConnectionRequest;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.ConnectionRequestResource;

public class ConnectionRequestResourceFromEntityAssembler {
    public static ConnectionRequestResource toResourceFromEntity(ConnectionRequest request) {
        return new ConnectionRequestResource(
                request.getId(),
                request.getAthleteId().userId(),
                request.getCoachId().userId(),
                request.getMessage(),
                request.getStatus().name(),
                request.getRespondedAt(),
                request.getResponseNote());
    }
}
