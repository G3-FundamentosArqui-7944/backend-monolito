package pe.edu.upc.bodymatch.nutrition.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.iam.interfaces.acl.IamContextFacade;
import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;

@Service("nutritionExternalIamService")
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public boolean existsUser(UserId userId) {
        return iamContextFacade.existsUserById(userId.userId());
    }
}
