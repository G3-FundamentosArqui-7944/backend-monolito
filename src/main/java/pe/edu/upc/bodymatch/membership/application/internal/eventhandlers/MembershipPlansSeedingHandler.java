package pe.edu.upc.bodymatch.membership.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.membership.domain.model.commands.SeedMembershipPlansCommand;
import pe.edu.upc.bodymatch.membership.domain.services.MembershipPlanCommandService;

@Service
public class MembershipPlansSeedingHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipPlansSeedingHandler.class);

    private final MembershipPlanCommandService planCommandService;

    public MembershipPlansSeedingHandler(MembershipPlanCommandService planCommandService) {
        this.planCommandService = planCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        LOGGER.info("Seeding membership plans if missing");
        planCommandService.handle(new SeedMembershipPlansCommand());
    }
}
