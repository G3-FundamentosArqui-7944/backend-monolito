package pe.edu.upc.bodymatch.matchmaking.interfaces.rest.transform;

import pe.edu.upc.bodymatch.matchmaking.domain.model.entities.AvailabilitySlot;
import pe.edu.upc.bodymatch.matchmaking.interfaces.rest.resources.AvailabilitySlotResource;

public class AvailabilitySlotResourceFromEntityAssembler {
    public static AvailabilitySlotResource toResourceFromEntity(AvailabilitySlot slot) {
        return new AvailabilitySlotResource(
                slot.getId(),
                slot.getSlot().dayOfWeek().name(),
                slot.getSlot().startTime().toString(),
                slot.getSlot().endTime().toString(),
                slot.isActive());
    }
}
