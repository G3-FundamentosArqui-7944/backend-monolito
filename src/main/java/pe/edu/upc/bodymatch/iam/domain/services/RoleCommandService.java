package pe.edu.upc.bodymatch.iam.domain.services;

import pe.edu.upc.bodymatch.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
