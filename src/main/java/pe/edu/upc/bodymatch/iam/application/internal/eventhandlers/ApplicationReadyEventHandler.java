package pe.edu.upc.bodymatch.iam.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.iam.domain.model.commands.SeedRolesCommand;
import pe.edu.upc.bodymatch.iam.domain.services.RoleCommandService;

import java.sql.Timestamp;

@Service
public class ApplicationReadyEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventHandler.class);

    private final RoleCommandService roleCommandService;

    public ApplicationReadyEventHandler(RoleCommandService roleCommandService) {
        this.roleCommandService = roleCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Verifying roles seeding for {} at {}", applicationName, getCurrentTimestamp());
        roleCommandService.handle(new SeedRolesCommand());
        LOGGER.info("Roles seeding finished for {} at {}", applicationName, getCurrentTimestamp());
    }

    private Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
