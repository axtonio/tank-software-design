package ru.mipt.bit.platformer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.mipt.bit.platformer.classes.KeyboardListener;
import ru.mipt.bit.platformer.graphics.HealthBarDisplay;
import ru.mipt.bit.platformer.graphics.HealthBarEntityRenderer;
import ru.mipt.bit.platformer.graphics.SpriteEntityRenderer;
import ru.mipt.bit.platformer.recommendation.DistanceBasedRecommendationClient;
import ru.mipt.bit.platformer.recommendation.RecommendationEngineAdapter;

@Configuration
public class GameConfig {

    @Bean
    public KeyboardListener keyboardListener() {
        return new KeyboardListener();
    }

    @Bean
    public DistanceBasedRecommendationClient distanceBasedRecommendationClient() {
        return new DistanceBasedRecommendationClient();
    }

    @Bean
    public RecommendationEngineAdapter recommendationEngineAdapter(
            DistanceBasedRecommendationClient recommendationClient) {
        return new RecommendationEngineAdapter(recommendationClient);
    }

    @Bean
    public SpriteEntityRenderer spriteEntityRenderer() {
        return new SpriteEntityRenderer();
    }

    @Bean
    public HealthBarEntityRenderer healthBarEntityRenderer(SpriteEntityRenderer spriteRenderer) {
        return new HealthBarEntityRenderer(spriteRenderer);
    }

    @Bean
    public HealthBarDisplay healthBarDisplay(SpriteEntityRenderer spriteRenderer,
            HealthBarEntityRenderer healthBarRenderer) {
        return new HealthBarDisplay(spriteRenderer, healthBarRenderer);
    }
}

