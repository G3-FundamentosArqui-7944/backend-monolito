package pe.edu.upc.bodymatch.matchmaking.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.RequestCoachingCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.RespondConnectionRequestCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetCoachClientsQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetConnectionRequestsByAthleteQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetConnectionRequestsByCoachQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.matchmaking.domain.services.ConnectionRequestCommandService;
import pe.edu.upc.bodymatch.matchmaking.domain.services.ConnectionRequestQueryService;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.ConnectionRequestResource;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.CreateConnectionRequestResource;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.RespondConnectionRequestResource;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform.ConnectionRequestResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/connection-requests", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Connection Requests", description = "Athlete-Coach connection requests")
public class ConnectionRequestsController {
    private final ConnectionRequestCommandService connectionRequestCommandService;
    private final ConnectionRequestQueryService connectionRequestQueryService;

    public ConnectionRequestsController(ConnectionRequestCommandService connectionRequestCommandService,
                                        ConnectionRequestQueryService connectionRequestQueryService) {
        this.connectionRequestCommandService = connectionRequestCommandService;
        this.connectionRequestQueryService = connectionRequestQueryService;
    }

    @PostMapping
    public ResponseEntity<ConnectionRequestResource> request(@RequestBody CreateConnectionRequestResource resource) {
        try {
            var command = new RequestCoachingCommand(new UserId(resource.athleteId()),
                    new UserId(resource.coachId()), resource.message());
            var result = connectionRequestCommandService.handle(command);
            if (result.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    ConnectionRequestResourceFromEntityAssembler.toResourceFromEntity(result.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<ConnectionRequestResource> respond(
            @PathVariable Long requestId,
            @RequestBody RespondConnectionRequestResource resource) {
        try {
            var command = new RespondConnectionRequestCommand(requestId, resource.approve(), resource.responseNote());
            var result = connectionRequestCommandService.handle(command);
            if (result.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(ConnectionRequestResourceFromEntityAssembler.toResourceFromEntity(result.get()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<List<ConnectionRequestResource>> byAthlete(@PathVariable Long athleteId) {
        var requests = connectionRequestQueryService.handle(new GetConnectionRequestsByAthleteQuery(new UserId(athleteId)));
        return ResponseEntity.ok(requests.stream()
                .map(ConnectionRequestResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<ConnectionRequestResource>> byCoach(@PathVariable Long coachId) {
        var requests = connectionRequestQueryService.handle(new GetConnectionRequestsByCoachQuery(new UserId(coachId)));
        return ResponseEntity.ok(requests.stream()
                .map(ConnectionRequestResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/coach/{coachId}/clients")
    public ResponseEntity<List<ConnectionRequestResource>> coachClients(@PathVariable Long coachId) {
        var requests = connectionRequestQueryService.handle(new GetCoachClientsQuery(new UserId(coachId)));
        return ResponseEntity.ok(requests.stream()
                .map(ConnectionRequestResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }
}
