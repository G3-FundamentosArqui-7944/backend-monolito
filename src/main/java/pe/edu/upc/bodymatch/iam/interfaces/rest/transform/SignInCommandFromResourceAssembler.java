package pe.edu.upc.bodymatch.iam.interfaces.rest.transform;

import pe.edu.upc.bodymatch.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.bodymatch.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.email(), resource.password());
    }
}
