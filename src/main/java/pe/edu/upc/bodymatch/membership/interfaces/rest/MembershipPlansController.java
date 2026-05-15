package pe.edu.upc.bodymatch.membership.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetAllMembershipPlansQuery;
import pe.edu.upc.bodymatch.membership.domain.model.queries.GetMembershipPlanByCodeQuery;
import pe.edu.upc.bodymatch.membership.domain.services.MembershipPlanCommandService;
import pe.edu.upc.bodymatch.membership.domain.services.MembershipPlanQueryService;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.CreateMembershipPlanResource;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.MembershipPlanResource;
import pe.edu.upc.bodymatch.membership.interfaces.rest.transform.CreateMembershipPlanCommandFromResourceAssembler;
import pe.edu.upc.bodymatch.membership.interfaces.rest.transform.MembershipPlanResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/membership-plans", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Membership Plans", description = "Catalog of subscription plans")
public class MembershipPlansController {
    private final MembershipPlanCommandService planCommandService;
    private final MembershipPlanQueryService planQueryService;

    public MembershipPlansController(MembershipPlanCommandService planCommandService,
                                     MembershipPlanQueryService planQueryService) {
        this.planCommandService = planCommandService;
        this.planQueryService = planQueryService;
    }

    @GetMapping
    public ResponseEntity<List<MembershipPlanResource>> getAll(
            @RequestParam(name = "onlyActive", defaultValue = "true") boolean onlyActive) {
        var plans = planQueryService.handle(new GetAllMembershipPlansQuery(onlyActive));
        var resources = plans.stream().map(MembershipPlanResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{code}")
    public ResponseEntity<MembershipPlanResource> getByCode(@PathVariable String code) {
        var plan = planQueryService.handle(new GetMembershipPlanByCodeQuery(code));
        if (plan.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(MembershipPlanResourceFromEntityAssembler.toResourceFromEntity(plan.get()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MembershipPlanResource> create(@RequestBody CreateMembershipPlanResource resource) {
        try {
            var command = CreateMembershipPlanCommandFromResourceAssembler.toCommandFromResource(resource);
            var plan = planCommandService.handle(command);
            if (plan.isEmpty()) return ResponseEntity.badRequest().build();
            return new ResponseEntity<>(
                    MembershipPlanResourceFromEntityAssembler.toResourceFromEntity(plan.get()),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
