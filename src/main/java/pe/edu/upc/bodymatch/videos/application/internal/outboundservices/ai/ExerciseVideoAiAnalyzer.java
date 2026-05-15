package pe.edu.upc.bodymatch.videos.application.internal.outboundservices.ai;

import pe.edu.upc.bodymatch.videos.application.internal.outboundservices.ai.dto.ExerciseAnalysisResult;

public interface ExerciseVideoAiAnalyzer {
    ExerciseAnalysisResult analyze(String exerciseName, String mimeType, byte[] videoContent);
}
