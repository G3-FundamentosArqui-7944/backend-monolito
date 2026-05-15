package pe.edu.upc.bodymatch.shared.infrastructure.storage;

public record StoredObject(String storageKey, String url, long sizeBytes, String contentType) {
}
