package pe.edu.upc.bodymatch.matchmaking.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.UserId;
import pe.edu.upc.bodymatch.membership.interfaces.acl.MembershipContextFacade;

@Service("matchmakingExternalMembershipService")
public class ExternalMembershipService {

    private final MembershipContextFacade membershipContextFacade;

    public ExternalMembershipService(MembershipContextFacade membershipContextFacade) {
        this.membershipContextFacade = membershipContextFacade;
    }

    public boolean hasActiveMembership(UserId userId) {
        return membershipContextFacade.hasActiveMembership(userId.userId());
    }
}
