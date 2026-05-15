package pe.edu.upc.bodymatch.matchmaking.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.matchmaking.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.CoachProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.AddCoachAvailabilityCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CreateCoachProfileCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.model.entities.AvailabilitySlot;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.DayOfWeekSlot;
import pe.edu.upc.bodymatch.matchmaking.domain.services.CoachProfileCommandService;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.CoachProfileRepository;

import java.util.Optional;

@Service
public class CoachProfileCommandServiceImpl implements CoachProfileCommandService {
    private final CoachProfileRepository coachProfileRepository;
    private final ExternalIamService externalIamService;

    public CoachProfileCommandServiceImpl(CoachProfileRepository coachProfileRepository,
                                          ExternalIamService externalIamService) {
        this.coachProfileRepository = coachProfileRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public Optional<CoachProfile> handle(CreateCoachProfileCommand command) {
        if (!externalIamService.existsUser(command.userId())) {
            throw new IllegalArgumentException("User does not exist: " + command.userId().userId());
        }
        if (!externalIamService.isCoach(command.userId())) {
            throw new IllegalStateException("User does not have ROLE_COACH");
        }
        if (coachProfileRepository.existsByUserId(command.userId())) {
            throw new IllegalStateException("Coach profile already exists for user");
        }
        var profile = new CoachProfile(
                command.userId(),
                command.biography(),
                command.yearsOfExperience(),
                command.hourlyRate(),
                command.currency(),
                command.specialties());
        coachProfileRepository.save(profile);
        return Optional.of(profile);
    }

    @Override
    @Transactional
    public Optional<CoachProfile> handle(AddCoachAvailabilityCommand command) {
        var profile = coachProfileRepository.findByUserId(command.coachId())
                .orElseThrow(() -> new IllegalArgumentException("Coach profile not found"));
        var slot = new AvailabilitySlot(new DayOfWeekSlot(command.dayOfWeek(), command.startTime(), command.endTime()));
        profile.addAvailability(slot);
        coachProfileRepository.save(profile);
        return Optional.of(profile);
    }
}
