package pe.edu.upc.bodymatch.iam.domain.model.commands;

import pe.edu.upc.bodymatch.iam.domain.model.entities.Role;

import java.util.List;

public record SignUpCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        List<Role> roles) {
}
