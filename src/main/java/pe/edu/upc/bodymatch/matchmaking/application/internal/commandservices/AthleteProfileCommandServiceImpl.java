package pe.edu.upc.bodymatch.matchmaking.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.bodymatch.matchmaking.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.AthleteProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.commands.CreateAthleteProfileCommand;
import pe.edu.upc.bodymatch.matchmaking.domain.services.AthleteProfileCommandService;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.AthleteProfileRepository;

import java.util.Optional;

@Service
public class AthleteProfileCommandServiceImpl implements AthleteProfileCommandService {
    private final AthleteProfileRepository athleteProfileRepository;
    private final ExternalIamService externalIamService;

    public AthleteProfileCommandServiceImpl(AthleteProfileRepository athleteProfileRepository,
                                            ExternalIamService externalIamService) {
        this.athleteProfileRepository = athleteProfileRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public Optional<AthleteProfile> handle(CreateAthleteProfileCommand command) {
        if (!externalIamService.existsUser(command.userId())) {
            throw new IllegalArgumentException("User does not exist: " + command.userId().userId());
        }
        if (!externalIamService.isAthlete(command.userId())) {
            throw new IllegalStateException("User does not have ROLE_ATHLETE");
        }
        if (athleteProfileRepository.existsByUserId(command.userId())) {
            throw new IllegalStateException("Athlete profile already exists for user");
        }
        var profile = new AthleteProfile(
                command.userId(),
                command.trainingLevel(),
                command.goals(),
                command.preferences());
        athleteProfileRepository.save(profile);
        return Optional.of(profile);
    }
}
