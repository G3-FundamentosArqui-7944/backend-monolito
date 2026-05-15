package pe.edu.upc.bodymatch.matchmaking.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.AddCoachAvailabilityCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetCoachByUserIdQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetRecommendedCoachesQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.SearchCoachesQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.Specialty;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.matchmaking.domain.services.CoachProfileCommandService;
import pe.edu.upc.bodymatch.matchmaking.domain.services.CoachProfileQueryService;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.AddAvailabilityResource;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.CoachProfileResource;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.CreateCoachProfileResource;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform.CoachProfileResourceFromEntityAssembler;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform.CreateCoachProfileCommandFromResourceAssembler;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/coaches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Coaches", description = "Coach profile and matchmaking endpoints")
public class CoachesController {
    private final CoachProfileCommandService coachProfileCommandService;
    private final CoachProfileQueryService coachProfileQueryService;

    public CoachesController(CoachProfileCommandService coachProfileCommandService,
                             CoachProfileQueryService coachProfileQueryService) {
        this.coachProfileCommandService = coachProfileCommandService;
        this.coachProfileQueryService = coachProfileQueryService;
    }

    @PostMapping
    public ResponseEntity<CoachProfileResource> createProfile(@RequestBody CreateCoachProfileResource resource) {
        try {
            var command = CreateCoachProfileCommandFromResourceAssembler.toCommandFromResource(resource);
            var profile = coachProfileCommandService.handle(command);
            if (profile.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    CoachProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CoachProfileResource> getByUserId(@PathVariable Long userId) {
        var coach = coachProfileQueryService.handle(new GetCoachByUserIdQuery(new UserId(userId)));
        if (coach.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(CoachProfileResourceFromEntityAssembler.toResourceFromEntity(coach.get()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CoachProfileResource>> search(
            @RequestParam(required = false) List<String> specialties,
            @RequestParam(required = false) Integer minYearsOfExperience,
            @RequestParam(required = false) BigDecimal maxHourlyRate,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(defaultValue = "true") boolean onlyAccepting) {
        Set<Specialty> specialtySet = specialties == null ? Set.of()
                : specialties.stream().map(Specialty::valueOf).collect(Collectors.toSet());
        var query = new SearchCoachesQuery(specialtySet, minYearsOfExperience, maxHourlyRate, minRating, onlyAccepting);
        var resources = coachProfileQueryService.handle(query).stream()
                .map(CoachProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/recommendations/{athleteId}")
    public ResponseEntity<List<CoachProfileResource>> recommendations(
            @PathVariable Long athleteId,
            @RequestParam(defaultValue = "10") int limit) {
        var query = new GetRecommendedCoachesQuery(new UserId(athleteId), limit);
        var resources = coachProfileQueryService.handle(query).stream()
                .map(CoachProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/{coachId}/availability")
    public ResponseEntity<CoachProfileResource> addAvailability(
            @PathVariable Long coachId,
            @RequestBody AddAvailabilityResource resource) {
        try {
            var command = new AddCoachAvailabilityCommand(
                    new UserId(coachId),
                    DayOfWeek.valueOf(resource.dayOfWeek()),
                    LocalTime.parse(resource.startTime()),
                    LocalTime.parse(resource.endTime()));
            var profile = coachProfileCommandService.handle(command);
            if (profile.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(CoachProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
