package pe.edu.upc.bodymatch.iam.interfaces.rest.transform;

import pe.edu.upc.bodymatch.iam.domain.model.aggregates.User;
import pe.edu.upc.bodymatch.iam.domain.model.entities.Role;
import pe.edu.upc.bodymatch.iam.interfaces.rest.resources.AuthenticatedUserResource;

import java.util.Set;
import java.util.stream.Collectors;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String accessToken, String refreshToken) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getStringName)
                .collect(Collectors.toSet());
        return new AuthenticatedUserResource(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                accessToken,
                refreshToken,
                roleNames);
    }
}
