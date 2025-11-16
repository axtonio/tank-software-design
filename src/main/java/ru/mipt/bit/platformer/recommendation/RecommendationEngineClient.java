package ru.mipt.bit.platformer.recommendation;

public interface RecommendationEngineClient {
    RecommendationAction getRecommendation(RecommendationRequest request);
}

