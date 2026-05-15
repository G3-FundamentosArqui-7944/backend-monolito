package pe.edu.upc.bodymatch.iam.domain.services;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import pe.edu.upc.bodymatch.iam.domain.model.aggregates.User;
import pe.edu.upc.bodymatch.iam.domain.model.commands.AssignRolesCommand;
import pe.edu.upc.bodymatch.iam.domain.model.commands.RefreshTokenCommand;
import pe.edu.upc.bodymatch.iam.domain.model.commands.RevokeRefreshTokenCommand;
import pe.edu.upc.bodymatch.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.bodymatch.iam.domain.model.commands.SignUpCommand;

import java.util.Optional;

public interface UserCommandService {
    Optional<User> handle(SignUpCommand command);
    Optional<ImmutableTriple<User, String, String>> handle(SignInCommand command);
    Optional<ImmutableTriple<User, String, String>> handle(RefreshTokenCommand command);
    void handle(RevokeRefreshTokenCommand command);
    Optional<User> handle(AssignRolesCommand command);
}
