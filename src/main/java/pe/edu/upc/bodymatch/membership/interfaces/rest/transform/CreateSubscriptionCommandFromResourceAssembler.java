package pe.edu.upc.bodymatch.membership.interfaces.rest.transform;

import pe.edu.upc.bodymatch.membership.domain.model.commands.CreateSubscriptionCommand;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.membership.interfaces.rest.resources.CreateSubscriptionResource;

public class CreateSubscriptionCommandFromResourceAssembler {
    public static CreateSubscriptionCommand toCommandFromResource(CreateSubscriptionResource resource) {
        return new CreateSubscriptionCommand(new UserId(resource.userId()), resource.planCode());
    }
}
