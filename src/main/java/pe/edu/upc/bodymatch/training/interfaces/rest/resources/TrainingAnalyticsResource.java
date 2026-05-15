package pe.edu.upc.bodymatch.training.interfaces.rest.resources;

public record TrainingAnalyticsResource(
        long totalWorkouts,
        long completedWorkouts,
        long abandonedWorkouts,
        long totalExerciseExecutions,
        long totalVolume,
        double averageVolumePerSession,
        double completionRate) {
}
