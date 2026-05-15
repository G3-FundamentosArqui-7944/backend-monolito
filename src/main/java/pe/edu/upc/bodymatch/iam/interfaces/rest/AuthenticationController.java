package pe.edu.upc.bodymatch.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.bodymatch.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.bodymatch.iam.domain.model.commands.RefreshTokenCommand;
import pe.edu.upc.bodymatch.iam.domain.model.commands.RevokeRefreshTokenCommand;
import pe.edu.upc.bodymatch.iam.domain.services.UserCommandService;
import pe.edu.upc.bodymatch.iam.interfaces.rest.resources.AuthenticatedUserResource;
import pe.edu.upc.bodymatch.iam.interfaces.rest.resources.RefreshTokenResource;
import pe.edu.upc.bodymatch.iam.interfaces.rest.resources.SignInResource;
import pe.edu.upc.bodymatch.iam.interfaces.rest.resources.SignUpResource;
import pe.edu.upc.bodymatch.iam.interfaces.rest.resources.TokenValidationResource;
import pe.edu.upc.bodymatch.iam.interfaces.rest.resources.UserResource;
import pe.edu.upc.bodymatch.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import pe.edu.upc.bodymatch.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import pe.edu.upc.bodymatch.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import pe.edu.upc.bodymatch.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication and Identity Management Endpoints")
public class AuthenticationController {
    private final UserCommandService userCommandService;
    private final TokenService tokenService;

    public AuthenticationController(UserCommandService userCommandService, TokenService tokenService) {
        this.userCommandService = userCommandService;
        this.tokenService = tokenService;
    }

    @PostMapping("/sign-up/athlete")
    public ResponseEntity<UserResource> signUpAthlete(@RequestBody SignUpResource resource) {
        var withRoles = new SignUpResource(
                resource.email(), resource.password(), resource.firstName(), resource.lastName(), resource.phone(),
                java.util.List.of("ROLE_ATHLETE"));
        return signUpInternal(withRoles, HttpStatus.CREATED);
    }

    @PostMapping("/sign-up/coach")
    public ResponseEntity<UserResource> signUpCoach(@RequestBody SignUpResource resource) {
        var withRoles = new SignUpResource(
                resource.email(), resource.password(), resource.firstName(), resource.lastName(), resource.phone(),
                java.util.List.of("ROLE_COACH"));
        return signUpInternal(withRoles, HttpStatus.CREATED);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResource> signUp(@RequestBody SignUpResource resource) {
        return signUpInternal(resource, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource resource) {
        var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(command);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        var triple = result.get();
        var authenticated = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(
                triple.getLeft(), triple.getMiddle(), triple.getRight());
        return ResponseEntity.ok(authenticated);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticatedUserResource> refreshToken(@RequestBody RefreshTokenResource resource) {
        var command = new RefreshTokenCommand(resource.refreshToken());
        var result = userCommandService.handle(command);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        var triple = result.get();
        var authenticated = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(
                triple.getLeft(), triple.getMiddle(), triple.getRight());
        return ResponseEntity.ok(authenticated);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(@RequestBody RefreshTokenResource resource) {
        userCommandService.handle(new RevokeRefreshTokenCommand(resource.refreshToken()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate-token")
    public ResponseEntity<TokenValidationResource> validateToken(@RequestBody RefreshTokenResource resource) {
        var token = resource.refreshToken();
        var valid = tokenService.validateToken(token);
        var email = valid ? tokenService.getEmailFromToken(token) : null;
        return ResponseEntity.ok(new TokenValidationResource(valid, email));
    }

    private ResponseEntity<UserResource> signUpInternal(SignUpResource resource, HttpStatus successStatus) {
        var command = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command);
        if (user.isEmpty()) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()), successStatus);
    }
}
