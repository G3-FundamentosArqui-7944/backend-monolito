package pe.edu.upc.bodymatch.matchmaking.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetAthleteByUserIdQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.matchmaking.domain.services.AthleteProfileCommandService;
import pe.edu.upc.bodymatch.matchmaking.domain.services.AthleteProfileQueryService;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.AthleteProfileResource;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.CreateAthleteProfileResource;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform.AthleteProfileResourceFromEntityAssembler;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform.CreateAthleteProfileCommandFromResourceAssembler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/athletes", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Athletes", description = "Athlete profile endpoints")
public class AthletesController {
    private final AthleteProfileCommandService athleteCommandService;
    private final AthleteProfileQueryService athleteQueryService;

    public AthletesController(AthleteProfileCommandService athleteCommandService,
                              AthleteProfileQueryService athleteQueryService) {
        this.athleteCommandService = athleteCommandService;
        this.athleteQueryService = athleteQueryService;
    }

    @PostMapping
    public ResponseEntity<AthleteProfileResource> createProfile(@RequestBody CreateAthleteProfileResource resource) {
        try {
            var command = CreateAthleteProfileCommandFromResourceAssembler.toCommandFromResource(resource);
            var profile = athleteCommandService.handle(command);
            if (profile.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    AthleteProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AthleteProfileResource> getByUserId(@PathVariable Long userId) {
        var athlete = athleteQueryService.handle(new GetAthleteByUserIdQuery(new UserId(userId)));
        if (athlete.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(AthleteProfileResourceFromEntityAssembler.toResourceFromEntity(athlete.get()));
    }
}
