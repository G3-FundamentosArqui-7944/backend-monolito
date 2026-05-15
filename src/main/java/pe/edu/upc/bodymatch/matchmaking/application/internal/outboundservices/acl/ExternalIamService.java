package pe.edu.upc.bodymatch.matchmaking.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.iam.interfaces.acl.IamContextFacade;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;

@Service("matchmakingExternalIamService")
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public boolean existsUser(UserId userId) {
        return iamContextFacade.existsUserById(userId.userId());
    }

    public boolean isCoach(UserId userId) {
        return iamContextFacade.isCoach(userId.userId());
    }

    public boolean isAthlete(UserId userId) {
        return iamContextFacade.isAthlete(userId.userId());
    }
}
