package pe.edu.upc.bodymatch.iam.interfaces.rest.resources;

import java.util.Set;

public record AuthenticatedUserResource(
        Long id,
        String email,
        String firstName,
        String lastName,
        String accessToken,
        String refreshToken,
        Set<String> roles) {
}
