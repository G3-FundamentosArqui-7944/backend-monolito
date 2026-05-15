package pe.edu.upc.bodymatch.training.interfaces.rest.transform;

import pe.edu.upc.bodymatch.training.domain.model.aggregates.ProgressRecord;
import pe.edu.upc.bodymatch.training.interfaces.rest.resources.ProgressRecordResource;

public class ProgressRecordResourceFromEntityAssembler {
    public static ProgressRecordResource toResourceFromEntity(ProgressRecord record) {
        return new ProgressRecordResource(
                record.getId(),
                record.getUserId().userId(),
                record.getMilestone(),
                record.getDescription(),
                record.getAchievedAt());
    }
}
