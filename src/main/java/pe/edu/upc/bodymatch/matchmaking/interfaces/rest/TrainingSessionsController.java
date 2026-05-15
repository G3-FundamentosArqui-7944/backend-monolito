package pe.edu.upc.bodymatch.matchmaking.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CancelTrainingSessionCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CompleteTrainingSessionCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.ScheduleTrainingSessionCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetTrainingSessionsByAthleteQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetTrainingSessionsByCoachQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.matchmaking.domain.services.TrainingSessionCommandService;
import pe.edu.upc.bodymatch.matchmaking.domain.services.TrainingSessionQueryService;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.CompleteTrainingSessionResource;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.ScheduleTrainingSessionResource;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.TrainingSessionResource;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform.TrainingSessionResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/training-sessions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Training Sessions", description = "Coach-led training sessions")
public class TrainingSessionsController {
    private final TrainingSessionCommandService trainingSessionCommandService;
    private final TrainingSessionQueryService trainingSessionQueryService;

    public TrainingSessionsController(TrainingSessionCommandService trainingSessionCommandService,
                                      TrainingSessionQueryService trainingSessionQueryService) {
        this.trainingSessionCommandService = trainingSessionCommandService;
        this.trainingSessionQueryService = trainingSessionQueryService;
    }

    @PostMapping
    public ResponseEntity<TrainingSessionResource> schedule(@RequestBody ScheduleTrainingSessionResource resource) {
        try {
            var command = new ScheduleTrainingSessionCommand(
                    new UserId(resource.athleteId()),
                    new UserId(resource.coachId()),
                    resource.scheduledAt(),
                    resource.durationMinutes(),
                    resource.location(),
                    resource.notes());
            var session = trainingSessionCommandService.handle(command);
            if (session.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    TrainingSessionResourceFromEntityAssembler.toResourceFromEntity(session.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{sessionId}/complete")
    public ResponseEntity<TrainingSessionResource> complete(
            @PathVariable Long sessionId,
            @RequestBody CompleteTrainingSessionResource resource) {
        try {
            var command = new CompleteTrainingSessionCommand(sessionId, resource.coachNotes());
            var session = trainingSessionCommandService.handle(command);
            if (session.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(TrainingSessionResourceFromEntityAssembler.toResourceFromEntity(session.get()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<TrainingSessionResource> cancel(@PathVariable Long sessionId) {
        try {
            var session = trainingSessionCommandService.handle(new CancelTrainingSessionCommand(sessionId));
            if (session.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(TrainingSessionResourceFromEntityAssembler.toResourceFromEntity(session.get()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<List<TrainingSessionResource>> byAthlete(@PathVariable Long athleteId) {
        var sessions = trainingSessionQueryService.handle(new GetTrainingSessionsByAthleteQuery(new UserId(athleteId)));
        return ResponseEntity.ok(sessions.stream()
                .map(TrainingSessionResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<TrainingSessionResource>> byCoach(@PathVariable Long coachId) {
        var sessions = trainingSessionQueryService.handle(new GetTrainingSessionsByCoachQuery(new UserId(coachId)));
        return ResponseEntity.ok(sessions.stream()
                .map(TrainingSessionResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }
}
