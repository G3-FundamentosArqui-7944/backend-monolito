package pe.edu.upc.bodymatch.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.iam.domain.model.commands.AssignRolesCommand;
import pe.edu.upc.bodymatch.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.bodymatch.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.bodymatch.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.bodymatch.iam.domain.services.UserCommandService;
import pe.edu.upc.bodymatch.iam.domain.services.UserQueryService;
import pe.edu.upc.bodymatch.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import pe.edu.upc.bodymatch.iam.interfaces.rest.resources.AssignRolesResource;
import pe.edu.upc.bodymatch.iam.interfaces.rest.resources.UserResource;
import pe.edu.upc.bodymatch.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User Management Endpoints")
public class UsersController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public UsersController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var users = userQueryService.handle(new GetAllUsersQuery());
        var resources = users.stream().map(UserResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var user = userQueryService.handle(new GetUserByIdQuery(userId));
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResource> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        var user = userQueryService.handle(new GetUserByEmailQuery(principal.getEmail()));
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
    }

    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResource> assignRoles(
            @PathVariable Long userId, @RequestBody AssignRolesResource resource) {
        var user = userCommandService.handle(new AssignRolesCommand(userId, resource.roles()));
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
    }
}
