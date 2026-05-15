package pe.edu.upc.bodymatch.membership.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.iam.interfaces.acl.IamContextFacade;
import pe.edu.upc.bodymatch.membership.domain.model.valueobjects.UserId;

@Service("membershipExternalIamService")
public class ExternalIamService {

    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public boolean existsUserById(UserId userId) {
        return iamContextFacade.existsUserById(userId.userId());
    }

    public String fetchEmailByUserId(UserId userId) {
        return iamContextFacade.fetchEmailByUserId(userId.userId());
    }
}
