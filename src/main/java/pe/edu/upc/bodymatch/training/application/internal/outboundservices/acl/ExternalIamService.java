package pe.edu.upc.bodymatch.training.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.iam.interfaces.acl.IamContextFacade;
import pe.edu.upc.bodymatch.training.domain.model.valueobjects.UserId;

@Service("trainingExternalIamService")
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public boolean existsUser(UserId userId) {
        return iamContextFacade.existsUserById(userId.userId());
    }
}
