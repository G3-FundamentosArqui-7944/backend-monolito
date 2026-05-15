package pe.edu.upc.bodymatch.nutrition.domain.model.commands;

import pe.edu.upc.bodymatch.nutrition.domain.model.valueobjects.UserId;

public record AnalyzeFoodImageCommand(
        UserId userId,
        String originalFilename,
        String contentType,
        byte[] imageContent) {
}
