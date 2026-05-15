package pe.edu.upc.bodymatch.videos.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.membership.interfaces.acl.MembershipContextFacade;
import pe.edu.upc.bodymatch.videos.domain.model.valueobjects.UserId;

@Service("videosExternalMembershipService")
public class ExternalMembershipService {
    private final MembershipContextFacade membershipContextFacade;

    public ExternalMembershipService(MembershipContextFacade membershipContextFacade) {
        this.membershipContextFacade = membershipContextFacade;
    }

    public boolean hasActiveMembership(UserId userId) {
        return membershipContextFacade.hasActiveMembership(userId.userId());
    }
}
