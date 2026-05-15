package pe.edu.upc.bodymatch.iam.domain.model.commands;

import java.util.List;

public record AssignRolesCommand(Long userId, List<String> roleNames) {
}
