package ru.mipt.bit.platformer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;

import ru.mipt.bit.platformer.application.commands.CommandsFactory;
import ru.mipt.bit.platformer.application.level.LevelGenerator;
import ru.mipt.bit.platformer.application.level.LogicalLevel;
import ru.mipt.bit.platformer.application.level.OccupancyManager;
import ru.mipt.bit.platformer.application.pipeline.GameLoop;
import ru.mipt.bit.platformer.infrastructure.ai.DistanceBasedRecommendationClient;
import ru.mipt.bit.platformer.infrastructure.ai.RecommendationEngineAdapter;
import ru.mipt.bit.platformer.infrastructure.commands.CommandsFactoryImpl;
import ru.mipt.bit.platformer.infrastructure.graphics.GraphicsLevel;
import ru.mipt.bit.platformer.infrastructure.graphics.HealthBarDisplay;
import ru.mipt.bit.platformer.infrastructure.graphics.HealthBarEntityRenderer;
import ru.mipt.bit.platformer.infrastructure.graphics.SpriteEntityRenderer;
import ru.mipt.bit.platformer.infrastructure.graphics.WorldRenderer;
import ru.mipt.bit.platformer.infrastructure.input.KeyboardListener;
import ru.mipt.bit.platformer.infrastructure.level.LevelGeneratorImpl;
import ru.mipt.bit.platformer.infrastructure.level.MapLayout;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.util.TileMovement;

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

    @Bean(destroyMethod = "dispose")
    public HealthBarEntityRenderer healthBarEntityRenderer(SpriteEntityRenderer spriteRenderer) {
        return new HealthBarEntityRenderer(spriteRenderer);
    }

    @Bean
    public HealthBarDisplay healthBarDisplay(SpriteEntityRenderer spriteRenderer,
            HealthBarEntityRenderer healthBarRenderer) {
        return new HealthBarDisplay(spriteRenderer, healthBarRenderer);
    }

    @Bean
    public GraphicsConfigurator graphicsConfigurator() {
        return new GraphicsConfigurator();
    }

    @Bean
    public MapLayout mapLayout() {
        return new MapLayout("mapLayout.txt");
    }

    @Bean
    public LevelGenerator levelGenerator(MapLayout mapLayout, RecommendationEngineAdapter recommendationEngineAdapter) {
        return new LevelGeneratorImpl(mapLayout, recommendationEngineAdapter);
    }

    @Bean(destroyMethod = "dispose")
    public TiledMap level() {
        return new TmxMapLoader().load("level.tmx");
    }

    @Bean
    public TiledMapTileLayer groundLayer(TiledMap level) {
        return GdxGameUtils.getSingleLayer(level);
    }

    @Bean(destroyMethod = "dispose")
    public Batch batch() {
        return new SpriteBatch();
    }

    @Bean
    public MapRenderer levelRenderer(TiledMap level, Batch batch) {
        return GdxGameUtils.createSingleLayerMapRenderer(level, batch);
    }

    @Bean
    public TileMovement tileMovement(TiledMapTileLayer groundLayer) {
        return new TileMovement(groundLayer, Interpolation.smooth);
    }

    @Bean
    public OccupancyManager occupancyManager(TiledMapTileLayer groundLayer) {
        return new OccupancyManager(groundLayer.getWidth(), groundLayer.getHeight());
    }

    @Bean(destroyMethod = "dispose")
    public GraphicsLevel graphicsLevel(GraphicsConfigurator graphicsConfigurator) {
        GraphicsLevel graphicsLevel = new GraphicsLevel();
        graphicsConfigurator.configure(graphicsLevel);
        return graphicsLevel;
    }

    @Bean
    public CommandsFactory commandsFactory(HealthBarDisplay healthBarDisplay) {
        return new CommandsFactoryImpl(healthBarDisplay);
    }

    @Bean
    public LogicalLevel logicalLevel(LevelGenerator levelGenerator, TiledMapTileLayer groundLayer,
            OccupancyManager occupancyManager, GraphicsLevel graphicsLevel, CommandsFactory commandsFactory) {
        LogicalLevel logicalLevel = levelGenerator.generate(groundLayer, occupancyManager, commandsFactory);
        logicalLevel.addObserver(graphicsLevel);
        return logicalLevel;
    }

    @Bean
    public WorldRenderer worldRenderer(GraphicsLevel graphicsLevel, TiledMapTileLayer groundLayer,
            TileMovement tileMovement, Batch batch, MapRenderer levelRenderer,
            HealthBarDisplay healthBarDisplay) {
        return new WorldRenderer(graphicsLevel, groundLayer, tileMovement, batch, levelRenderer,
                healthBarDisplay);
    }

    @Bean
    public GameLoop gameLoop(LogicalLevel logicalLevel, KeyboardListener keyboardListener,
            CommandsFactory commandsFactory) {
        return new GameLoop(logicalLevel, keyboardListener, commandsFactory);
    }
}
