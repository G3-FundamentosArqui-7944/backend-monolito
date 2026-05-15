package pe.edu.upc.bodymatch.iam.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.iam.domain.model.commands.SeedRolesCommand;
import pe.edu.upc.bodymatch.iam.domain.model.entities.Role;
import pe.edu.upc.bodymatch.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.bodymatch.iam.domain.services.RoleCommandService;
import pe.edu.upc.bodymatch.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

import java.util.Arrays;

@Service
public class RoleCommandServiceImpl implements RoleCommandService {
    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void handle(SeedRolesCommand command) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(role));
            }
        });
    }
}
