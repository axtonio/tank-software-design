package ru.mipt.bit.platformer;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;

import ru.mipt.bit.platformer.ai.RandomAIController;
import ru.mipt.bit.platformer.classes.Direction;
import ru.mipt.bit.platformer.classes.Graphics;
import ru.mipt.bit.platformer.classes.KeyboardListener;
import ru.mipt.bit.platformer.classes.MapLayout;
import ru.mipt.bit.platformer.classes.MovableEntity;
import ru.mipt.bit.platformer.classes.Positionable;
import ru.mipt.bit.platformer.classes.Tank;
import ru.mipt.bit.platformer.classes.Tree;
import ru.mipt.bit.platformer.commands.Command;
import ru.mipt.bit.platformer.commands.MoveCommand;
import ru.mipt.bit.platformer.commands.ShootCommand;
import ru.mipt.bit.platformer.commands.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.graphics.EntityRenderer;
import ru.mipt.bit.platformer.graphics.GraphicsLevel;
import ru.mipt.bit.platformer.graphics.HealthBarDisplay;
import ru.mipt.bit.platformer.graphics.HealthBarEntityRenderer;
import ru.mipt.bit.platformer.graphics.SpriteEntityRenderer;
import ru.mipt.bit.platformer.logic.LogicalLevel;
import ru.mipt.bit.platformer.logic.OccupancyManager;
import ru.mipt.bit.platformer.recommendation.DistanceBasedRecommendationClient;
import ru.mipt.bit.platformer.recommendation.RecommendationEngineAdapter;
import ru.mipt.bit.platformer.util.TileMovement;

public class GameDesktopLauncher implements ApplicationListener {
    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;
    private TiledMapTileLayer groundLayer;
    private MapLayout mapLayout;
    private OccupancyManager occupancy;
    private LogicalLevel logicalLevel;
    private GraphicsLevel graphicsLevel;
    private SpriteEntityRenderer spriteRenderer;
    private HealthBarEntityRenderer healthBarRenderer;
    private HealthBarDisplay healthBarDisplay;
    private KeyboardListener keyboardListener;
    private Graphics tankPrototype;
    private Graphics treePrototype;
    private Graphics bulletPrototype;
    private final ArrayList<Command> pendingCommands = new ArrayList<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        keyboardListener = new KeyboardListener();
        tankPrototype = new Graphics("images/tank_blue.png");
        treePrototype = new Graphics("images/greenTree.png");
        bulletPrototype = new Graphics("images/bullet.png");

        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        occupancy = new OccupancyManager(groundLayer.getWidth(), groundLayer.getHeight());

        mapLayout = new MapLayout("mapLayout.txt");

        GridPoint2 playerCoordinates = mapLayout.layout.get("Player").get(0);
        Tank player = new Tank(playerCoordinates.x, playerCoordinates.y, 0.4f);

        ArrayList<Tree> obstacles = new ArrayList<>();
        for (GridPoint2 obstacleCoordinates : mapLayout.layout.get("Obstacles")) {
            obstacles.add(new Tree(obstacleCoordinates.x, obstacleCoordinates.y));
        }
        java.util.HashSet<GridPoint2> staticObs = new java.util.HashSet<>();
        for (Positionable obstacle : obstacles) {
            staticObs.add(new GridPoint2(obstacle.coordinates));
        }
        occupancy.setStaticObstacles(staticObs);

        ArrayList<Tank> enemies = new ArrayList<>();
        ArrayList<RandomAIController> enemyAI = new ArrayList<>();
        java.util.Random rnd = new java.util.Random();
        int enemyCount = 2;
        java.util.HashSet<String> occupied = new java.util.HashSet<>();
        occupied.add(player.coordinates.x + "," + player.coordinates.y);
        for (Positionable obstacle : obstacles) {
            occupied.add(obstacle.coordinates.x + "," + obstacle.coordinates.y);
        }
        for (int e = 0; e < enemyCount; e++) {
            int attempts = 0;
            while (attempts < 1000) {
                int x = rnd.nextInt(groundLayer.getWidth());
                int y = rnd.nextInt(groundLayer.getHeight());
                String key = x + "," + y;
                if (!occupied.contains(key)) {
                    Tank enemy = new Tank(x, y, 0.4f);
                    enemies.add(enemy);
                    enemyAI.add(new RandomAIController());
                    occupied.add(key);
                    break;
                }
                attempts++;
            }
        }
        RecommendationEngineAdapter recommendationAdapter = new RecommendationEngineAdapter(
                new DistanceBasedRecommendationClient());

        logicalLevel = new LogicalLevel(player, obstacles, enemies, enemyAI, occupancy, recommendationAdapter);
        player.setProjectileRegistry(logicalLevel);
        for (Tank enemy : enemies) {
            enemy.setProjectileRegistry(logicalLevel);
        }

        graphicsLevel = new GraphicsLevel(tankPrototype, treePrototype, bulletPrototype);
        logicalLevel.addObserver(graphicsLevel);

        spriteRenderer = new SpriteEntityRenderer();
        healthBarRenderer = new HealthBarEntityRenderer(spriteRenderer);
        healthBarDisplay = new HealthBarDisplay(spriteRenderer, healthBarRenderer);
    }

    private void glClearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void startRendering() {
        // clear the screen
        glClearScreen();

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();
    }

    private void finishRendering() {
        // submit all drawing requests
        batch.end();
    }

    private void renderMovableEntities(EntityRenderer renderer) {
        for (Map.Entry<MovableEntity, Graphics> entry : graphicsLevel.getMovableEntries()) {
            MovableEntity entity = entry.getKey();
            Graphics graphics = entry.getValue();
            tileMovement.moveRectangleBetweenTileCenters(graphics.rectangle, entity.coordinates,
                    entity.destinationCoordinates, entity.movementProgress);
            renderer.render(batch, entity, graphics);
        }
    }

    private void renderStaticObjects() {
        for (Map.Entry<Positionable, Graphics> entry : graphicsLevel.getStaticEntries()) {
            Positionable object = entry.getKey();
            Graphics graphics = entry.getValue();
            moveRectangleAtTileCenter(groundLayer, graphics.rectangle, object.coordinates);
            drawTextureRegionUnscaled(batch, graphics.graphics, graphics.rectangle, 0f);
        }
    }

    private void collectCommands() {
        pendingCommands.clear();
        Tank player = logicalLevel.getPlayer();
        if (player == null || player.getHealth() <= 0) {
            logicalLevel.collectEnemyCommands(pendingCommands);
            return;
        }
        Direction direction = keyboardListener.captureMovementKey();
        pendingCommands.add(new MoveCommand(player, direction, logicalLevel.getOccupancy()));
        if (keyboardListener.isFireButtonPressed()) {
            pendingCommands.add(new ShootCommand(player));
        }
        logicalLevel.collectEnemyCommands(pendingCommands);
    }

    private void executeCommands() {
        for (Command command : pendingCommands) {
            if (command != null) {
                command.execute();
            }
        }
        pendingCommands.clear();
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Keys.L)) {
            new ToggleHealthBarCommand(healthBarDisplay).execute();
        }
        collectCommands();
        executeCommands();
        logicalLevel.tick(Gdx.graphics.getDeltaTime());

        startRendering();
        EntityRenderer renderer = healthBarDisplay.getRenderer();
        renderMovableEntities(renderer);
        renderStaticObjects();
        finishRendering();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        level.dispose();
        batch.dispose();
        healthBarRenderer.dispose();
        if (graphicsLevel != null) {
            graphicsLevel.dispose();
        }
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
