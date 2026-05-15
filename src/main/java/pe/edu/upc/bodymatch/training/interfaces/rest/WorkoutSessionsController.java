package pe.edu.upc.bodymatch.training.interfaces.rest;

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
import pe.edu.upc.bodymatch.training.domain.model.commands.AddExerciseExecutionCommand;
import pe.edu.upc.bodymatch.training.domain.model.commands.CompleteWorkoutSessionCommand;
import pe.edu.upc.bodymatch.training.domain.model.commands.StartWorkoutSessionCommand;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetWorkoutSessionByIdQuery;
import pe.edu.upc.bodymatch.training.domain.model.queries.GetWorkoutSessionsByUserQuery;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.training.domain.services.WorkoutSessionCommandService;
import pe.edu.upc.bodymatch.training.domain.services.WorkoutSessionQueryService;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.AddExerciseExecutionResource;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.StartWorkoutSessionResource;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.WorkoutSessionResource;
import pe.edu.upc.bodymatch.training.interfaces.rest.transform.WorkoutSessionResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/workout-sessions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Workout Sessions", description = "Workout session tracking endpoints")
public class WorkoutSessionsController {
    private final WorkoutSessionCommandService workoutSessionCommandService;
    private final WorkoutSessionQueryService workoutSessionQueryService;

    public WorkoutSessionsController(WorkoutSessionCommandService workoutSessionCommandService,
                                     WorkoutSessionQueryService workoutSessionQueryService) {
        this.workoutSessionCommandService = workoutSessionCommandService;
        this.workoutSessionQueryService = workoutSessionQueryService;
    }

    @PostMapping
    public ResponseEntity<WorkoutSessionResource> start(@RequestBody StartWorkoutSessionResource resource) {
        try {
            var command = new StartWorkoutSessionCommand(
                    new UserId(resource.userId()), resource.title(), resource.startedAt(), resource.notes());
            var session = workoutSessionCommandService.handle(command);
            if (session.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    WorkoutSessionResourceFromEntityAssembler.toResourceFromEntity(session.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{sessionId}/exercises")
    public ResponseEntity<WorkoutSessionResource> addExercise(
            @PathVariable Long sessionId,
            @RequestBody AddExerciseExecutionResource resource) {
        try {
            var command = new AddExerciseExecutionCommand(
                    sessionId, resource.exerciseName(), resource.sets(), resource.reps(),
                    resource.load(), resource.loadUnit(),
                    resource.durationSeconds(), resource.restSeconds(), resource.notes());
            var session = workoutSessionCommandService.handle(command);
            if (session.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(WorkoutSessionResourceFromEntityAssembler.toResourceFromEntity(session.get()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{sessionId}/complete")
    public ResponseEntity<WorkoutSessionResource> complete(@PathVariable Long sessionId) {
        try {
            var session = workoutSessionCommandService.handle(new CompleteWorkoutSessionCommand(sessionId));
            if (session.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(WorkoutSessionResourceFromEntityAssembler.toResourceFromEntity(session.get()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<WorkoutSessionResource> getById(@PathVariable Long sessionId) {
        var session = workoutSessionQueryService.handle(new GetWorkoutSessionByIdQuery(sessionId));
        if (session.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(WorkoutSessionResourceFromEntityAssembler.toResourceFromEntity(session.get()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkoutSessionResource>> getByUser(@PathVariable Long userId) {
        var sessions = workoutSessionQueryService.handle(new GetWorkoutSessionsByUserQuery(new UserId(userId)));
        return ResponseEntity.ok(sessions.stream()
                .map(WorkoutSessionResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }
}
