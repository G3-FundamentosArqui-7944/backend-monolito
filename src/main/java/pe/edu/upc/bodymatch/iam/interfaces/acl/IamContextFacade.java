package pe.edu.upc.bodymatch.iam.interfaces.acl;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.bodymatch.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.bodymatch.iam.domain.model.entities.Role;
import pe.edu.upc.bodymatch.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.bodymatch.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.bodymatch.iam.domain.services.UserCommandService;
import pe.edu.upc.bodymatch.iam.domain.services.UserQueryService;
import pe.edu.upc.bodymatch.iam.interfaces.rest.resources.UserResource;
import pe.edu.upc.bodymatch.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class IamContextFacade {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final TokenService tokenService;

    public IamContextFacade(UserCommandService userCommandService,
                            UserQueryService userQueryService,
                            TokenService tokenService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
        this.tokenService = tokenService;
    }

    public Long createUser(String email, String password, String firstName, String lastName, String phone) {
        var defaultRole = Role.toRoleFromName("ROLE_ATHLETE");
        var command = new SignUpCommand(email, password, firstName, lastName, phone, List.of(defaultRole));
        var result = userCommandService.handle(command);
        return result.map(u -> u.getId()).orElse(0L);
    }

    public Long createUser(String email, String password, String firstName, String lastName, String phone, List<String> roleNames) {
        if (roleNames == null) roleNames = new ArrayList<>();
        var roles = roleNames.stream().map(Role::toRoleFromName).toList();
        var command = new SignUpCommand(email, password, firstName, lastName, phone, roles);
        var result = userCommandService.handle(command);
        return result.map(u -> u.getId()).orElse(0L);
    }

    public Optional<UserResource> fetchUserById(Long userId) {
        return userQueryService.handle(new GetUserByIdQuery(userId))
                .map(UserResourceFromEntityAssembler::toResourceFromEntity);
    }

    public Long fetchUserIdByEmail(String email) {
        return userQueryService.handle(new GetUserByEmailQuery(email))
                .map(u -> u.getId()).orElse(0L);
    }

    public boolean existsUserByEmailAndIdIsNot(String email, Long id) {
        var user = userQueryService.handle(new GetUserByEmailQuery(email));
        if (user.isEmpty()) return false;
        return !Objects.equals(user.get().getId(), id);
    }

    public boolean existsUserById(Long id) {
        return userQueryService.handle(new GetUserByIdQuery(id)).isPresent();
    }

    public String fetchEmailByUserId(Long userId) {
        return userQueryService.handle(new GetUserByIdQuery(userId))
                .map(u -> u.getEmail()).orElse(Strings.EMPTY);
    }

    public boolean existsUserByRole(Long userId, String roleName) {
        var user = userQueryService.handle(new GetUserByIdQuery(userId));
        return user.map(u -> u.hasRole(roleName)).orElse(false);
    }

    public boolean isAthlete(Long userId) {
        return existsUserByRole(userId, "ROLE_ATHLETE");
    }

    public boolean isCoach(Long userId) {
        return existsUserByRole(userId, "ROLE_COACH");
    }

    public boolean validateAccessToken(String token) {
        return tokenService.validateToken(token);
    }

    public Optional<Long> resolveUserIdFromToken(String token) {
        if (!tokenService.validateToken(token)) return Optional.empty();
        var email = tokenService.getEmailFromToken(token);
        var userId = fetchUserIdByEmail(email);
        return userId.equals(0L) ? Optional.empty() : Optional.of(userId);
    }
}
