package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources;

import java.util.Set;

public record AthleteProfileResource(
        Long id,
        Long userId,
        String trainingLevel,
        Set<String> goals,
        String preferences) {
}
