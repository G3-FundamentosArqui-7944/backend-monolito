package pe.edu.upc.bodymatch.shared.infrastructure.ai.gemini;

public interface GeminiClient {
    String generateText(String model, String prompt);
    String generateFromMultimodal(String model, String prompt, String mimeType, byte[] inlineData);
}
