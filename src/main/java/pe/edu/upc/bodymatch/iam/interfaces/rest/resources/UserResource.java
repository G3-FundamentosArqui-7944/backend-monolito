package pe.edu.upc.bodymatch.iam.interfaces.rest.resources;

import java.util.List;

public record UserResource(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        boolean active,
        boolean emailVerified,
        List<String> roles) {
}
