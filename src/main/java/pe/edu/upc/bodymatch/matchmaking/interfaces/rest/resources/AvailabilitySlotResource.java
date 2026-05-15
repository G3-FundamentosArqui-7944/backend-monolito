package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources;

public record AvailabilitySlotResource(
        Long id,
        String dayOfWeek,
        String startTime,
        String endTime,
        boolean active) {
}
