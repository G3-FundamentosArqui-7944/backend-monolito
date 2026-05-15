package pe.edu.upc.bodymatch.training.domain.model.readmodel;

public record TrainingAnalytics(
        long totalWorkouts,
        long completedWorkouts,
        long abandonedWorkouts,
        long totalExerciseExecutions,
        long totalVolume,
        double averageVolumePerSession,
        double completionRate) {
}
