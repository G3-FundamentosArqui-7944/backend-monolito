package pe.edu.upc.bodymatch.matchmaking.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.AthleteProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.aggregates.CoachProfile;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetCoachByUserIdQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.GetRecommendedCoachesQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.queries.SearchCoachesQuery;
import pe.edu.upc.bodymatch.matchmaking.domain.model.valueobjects.Specialty;
import pe.edu.upc.bodymatch.matchmaking.domain.services.CoachProfileQueryService;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.AthleteProfileRepository;
import pe.edu.upc.bodymatch.matchmaking.infrastructure.persistence.jpa.repositories.CoachProfileRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CoachProfileQueryServiceImpl implements CoachProfileQueryService {

    private final CoachProfileRepository coachProfileRepository;
    private final AthleteProfileRepository athleteProfileRepository;

    public CoachProfileQueryServiceImpl(CoachProfileRepository coachProfileRepository,
                                        AthleteProfileRepository athleteProfileRepository) {
        this.coachProfileRepository = coachProfileRepository;
        this.athleteProfileRepository = athleteProfileRepository;
    }

    @Override
    public List<CoachProfile> handle(SearchCoachesQuery query) {
        var coaches = query.onlyAccepting()
                ? coachProfileRepository.findAllByAcceptingClientsTrue()
                : coachProfileRepository.findAll();
        return coaches.stream()
                .filter(c -> matchesSpecialties(c, query.specialties()))
                .filter(c -> query.minYearsOfExperience() == null || c.getYearsOfExperience() >= query.minYearsOfExperience())
                .filter(c -> query.maxHourlyRate() == null || c.getHourlyRate().compareTo(query.maxHourlyRate()) <= 0)
                .filter(c -> query.minRating() == null || c.getAverageRating().compareTo(query.minRating()) >= 0)
                .sorted(Comparator.comparing(CoachProfile::getAverageRating).reversed())
                .toList();
    }

    @Override
    public Optional<CoachProfile> handle(GetCoachByUserIdQuery query) {
        return coachProfileRepository.findByUserId(query.userId());
    }

    @Override
    public List<CoachProfile> handle(GetRecommendedCoachesQuery query) {
        var athleteOpt = athleteProfileRepository.findByUserId(query.athleteId());
        var allCoaches = coachProfileRepository.findAllByAcceptingClientsTrue();
        if (athleteOpt.isEmpty()) {
            return allCoaches.stream()
                    .sorted(Comparator.comparing(CoachProfile::getAverageRating).reversed())
                    .limit(query.limit() <= 0 ? 10 : query.limit())
                    .toList();
        }
        AthleteProfile athlete = athleteOpt.get();
        return allCoaches.stream()
                .sorted(Comparator.comparingInt((CoachProfile c) -> -scoreFor(c, athlete))
                        .thenComparing(CoachProfile::getAverageRating, Comparator.reverseOrder()))
                .limit(query.limit() <= 0 ? 10 : query.limit())
                .toList();
    }

    private boolean matchesSpecialties(CoachProfile coach, java.util.Set<Specialty> requested) {
        if (requested == null || requested.isEmpty()) return true;
        return coach.getSpecialties().stream().anyMatch(requested::contains);
    }

    private int scoreFor(CoachProfile coach, AthleteProfile athlete) {
        int score = 0;
        var goalSpecialties = goalsToSpecialties(athlete);
        for (var spec : coach.getSpecialties()) {
            if (goalSpecialties.contains(spec)) score += 5;
        }
        if (coach.getYearsOfExperience() >= 5) score += 2;
        if (coach.getAverageRating().compareTo(BigDecimal.valueOf(4)) >= 0) score += 3;
        return score;
    }

    private java.util.Set<Specialty> goalsToSpecialties(AthleteProfile athlete) {
        var result = new java.util.HashSet<Specialty>();
        athlete.getGoals().forEach(goal -> {
            switch (goal) {
                case WEIGHT_LOSS -> { result.add(Specialty.HIIT); result.add(Specialty.CARDIO); result.add(Specialty.NUTRITION_COACHING); }
                case MUSCLE_GAIN -> { result.add(Specialty.STRENGTH_TRAINING); result.add(Specialty.BODYBUILDING); }
                case STRENGTH -> { result.add(Specialty.POWERLIFTING); result.add(Specialty.STRENGTH_TRAINING); }
                case ENDURANCE -> { result.add(Specialty.CARDIO); result.add(Specialty.HIIT); }
                case MOBILITY -> { result.add(Specialty.MOBILITY); result.add(Specialty.YOGA); }
                case GENERAL_FITNESS -> { result.add(Specialty.CROSSFIT); result.add(Specialty.HIIT); }
                case COMPETITION_PREP -> { result.add(Specialty.BODYBUILDING); result.add(Specialty.STRENGTH_TRAINING); result.add(Specialty.NUTRITION_COACHING); }
            }
        });
        return result;
    }
}
