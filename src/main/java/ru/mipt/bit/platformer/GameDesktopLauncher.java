package ru.mipt.bit.platformer;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
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
import ru.mipt.bit.platformer.classes.MapLayout;
import ru.mipt.bit.platformer.classes.MovableEntity;
import ru.mipt.bit.platformer.classes.Positionable;
import ru.mipt.bit.platformer.classes.Tank;
import ru.mipt.bit.platformer.classes.Tree;
import ru.mipt.bit.platformer.commands.MoveCommand;
import ru.mipt.bit.platformer.logic.OccupancyManager;
import ru.mipt.bit.platformer.util.TileMovement;

public class GameDesktopLauncher implements ApplicationListener {
    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;
    private MapLayout mapLayout;

    private MovableEntity player;
    private ArrayList<Positionable> obstacles;
    private Graphics playerGraphics;
    private ArrayList<Graphics> obstaclesGraphics;
    private ArrayList<Tank> enemies;
    private ArrayList<Graphics> enemyGraphics;
    private ArrayList<RandomAIController> enemyAI;
    private OccupancyManager occupancy;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        occupancy = new OccupancyManager(groundLayer.getWidth(), groundLayer.getHeight());

        // ========== CHOOSE GENERATION MODE ==========
        // MODE 1: Random generation with random player position and obstacles
        // mapLayout = new MapLayout(groundLayer);

        // MODE 2: File-based generation - reads from mapLayout.txt
        // Format: T = Tree (obstacle), X = Player start, _ = empty cell
        mapLayout = new MapLayout("mapLayout.txt");
        // ============================================

        GridPoint2 playerCoordinates = mapLayout.layout.get("Player").get(0);
        player = new Tank(playerCoordinates.x, playerCoordinates.y, 0.4f);

        obstacles = new ArrayList<>();
        for (GridPoint2 obstacleCoordinates : mapLayout.layout.get("Obstacles")) {
            obstacles.add(new Tree(obstacleCoordinates.x, obstacleCoordinates.y));
        }
        java.util.HashSet<GridPoint2> staticObs = new java.util.HashSet<>();
        for (Positionable o : obstacles) {
            staticObs.add(new GridPoint2(o.coordinates));
        }
        occupancy.setStaticObstacles(staticObs);

        playerGraphics = new Graphics("images/tank_blue.png");
        obstaclesGraphics = new ArrayList<>();
        for (int i = 0; i < obstacles.size(); i++) {
            obstaclesGraphics.add(new Graphics("images/greenTree.png"));
        }

        for (int i = 0; i < obstacles.size(); i++) {
            Positionable obstacle = obstacles.get(i);
            Graphics obstacleG = obstaclesGraphics.get(i);
            moveRectangleAtTileCenter(groundLayer, obstacleG.rectangle, obstacle.coordinates);
        }

        enemies = new ArrayList<>();
        enemyGraphics = new ArrayList<>();
        enemyAI = new ArrayList<>();

        // Generate random enemy tanks on empty tiles
        java.util.Random rnd = new java.util.Random();
        int enemyCount = 3;
        java.util.HashSet<String> occupied = new java.util.HashSet<>();
        occupied.add(player.coordinates.x + "," + player.coordinates.y);
        for (Positionable o : obstacles) {
            occupied.add(o.coordinates.x + "," + o.coordinates.y);
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
                    enemyGraphics.add(new Graphics("images/tank_blue.png"));
                    enemyAI.add(new RandomAIController());
                    occupied.add(key);
                    break;
                }
                attempts++;
            }
        }
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

    private void renderPlayer() {
        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();
        player.movementProgress = continueProgress(player.movementProgress, deltaTime, player.movementSpeed);
        if (isEqual(player.movementProgress, 1f)) {
            // record that the player has reached his/her destination
            player.coordinates.set(player.destinationCoordinates);
            occupancy.release(player);
        }
        if (obstacles != null) {
            // keyboard-driven move via command with occupancy and bounds
            ru.mipt.bit.platformer.classes.KeyboardListener kl = new ru.mipt.bit.platformer.classes.KeyboardListener();
            ru.mipt.bit.platformer.classes.Direction d = kl.captureMovementKey();
            new MoveCommand((MovableEntity) player, d, occupancy).execute();

            tileMovement.moveRectangleBetweenTileCenters(playerGraphics.rectangle, player.coordinates,
                    player.destinationCoordinates, player.movementProgress);
        }
        drawTextureRegionUnscaled(batch, playerGraphics.graphics, playerGraphics.rectangle, player.rotation);
    }

    private void renderEnemies(TiledMapTileLayer groundLayer) {
        if (enemies == null)
            return;
        float deltaTime = Gdx.graphics.getDeltaTime();
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            Graphics g = enemyGraphics.get(i);
            enemy.movementProgress = continueProgress(enemy.movementProgress, deltaTime, enemy.movementSpeed);
            if (isEqual(enemy.movementProgress, 1f)) {
                enemy.coordinates.set(enemy.destinationCoordinates);
                occupancy.release(enemy);
                // choose a random direction and try to move
                Direction dir = enemyAI.get(i).chooseDirection();
                new MoveCommand(enemy, dir, occupancy).execute();
            } else {
                // keep reservation updated during move
                occupancy.updateReservation(enemy, enemy.coordinates, enemy.destinationCoordinates);
            }
            tileMovement.moveRectangleBetweenTileCenters(g.rectangle, enemy.coordinates,
                    enemy.destinationCoordinates, enemy.movementProgress);
            drawTextureRegionUnscaled(batch, g.graphics, g.rectangle, enemy.rotation);
        }
    }

    private void renderObstacles() {
        if (obstaclesGraphics != null) {
            for (int i = 0; i < obstaclesGraphics.size(); i++) {
                Graphics obstacleG = obstaclesGraphics.get(i);
                drawTextureRegionUnscaled(batch, obstacleG.graphics, obstacleG.rectangle, 0f);
            }
        }
    }

    @Override
    public void render() {
        startRendering();

        // render player
        renderPlayer();

        // render obstacles
        renderObstacles();

        // render enemies
        renderEnemies((TiledMapTileLayer) level.getLayers().get(0));

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
        // dispose of all the native resources (classes which implement
        // com.badlogic.gdx.utils.Disposable)
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
