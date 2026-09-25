package ru.mipt.bit.platformer.infrastructure.ai;

public interface RecommendationEngineClient {
    RecommendationAction getRecommendation(RecommendationRequest request);
}
