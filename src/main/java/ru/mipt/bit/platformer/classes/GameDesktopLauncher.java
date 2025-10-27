package ru.mipt.bit.platformer.classes;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.badlogic.gdx.math.Interpolation;

import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.util.TileMovement;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;
    private static final boolean USE_RANDOM_LEVEL = true;

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private Tank tank;
    private ArrayList<Tree> trees;
    private HashMap<String, Graphics> graphicsMap;
    private InputController inputController;
    private ArrayList<Graphics> treeGraphics;

    @Override
    public void create() {
        batch = new SpriteBatch();
        inputController = new KeyboardListener();

        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        MapLayout mapLayout;
        if (USE_RANDOM_LEVEL) {
            mapLayout = new MapLayout(groundLayer);
        } else {
            mapLayout = new MapLayout("mapLayout.txt");
        }

        trees = new ArrayList<>();
        ArrayList<Graphics> treeGraphicsList = new ArrayList<>();
        for (var treePosition : mapLayout.layout.get("Obstacles")) {
            Tree tree = new Tree(treePosition.x, treePosition.y);
            trees.add(tree);
            Graphics treeGfx = new Graphics("images/greenTree.png");
            treeGraphicsList.add(treeGfx);
            moveRectangleAtTileCenter(groundLayer, treeGfx.boundingBox, tree.coordinates);
        }
        treeGraphics = treeGraphicsList;

        var tankPosition = mapLayout.layout.get("Player").get(0);
        tank = new Tank(tankPosition.x, tankPosition.y, MOVEMENT_SPEED);
        Graphics tankGfx = new Graphics("images/tank_blue.png");
        moveRectangleAtTileCenter(groundLayer, tankGfx.boundingBox, tank.coordinates);
        graphicsMap = new HashMap<>();
        graphicsMap.put("tank", tankGfx);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        Direction direction = inputController.getDirection();
        GameObject[] obstacles = trees.toArray(new GameObject[0]);

        tank.tryMove(direction, obstacles);

        if (inputController.isFireButtonPressed()) {
            tank.fire();
        }

        Graphics tankGfx = graphicsMap.get("tank");
        tileMovement.moveRectangleBetweenTileCenters(tankGfx.boundingBox, tank.coordinates,
                tank.destinationCoordinates, tank.movementProgress);

        tank.movementProgress = continueProgress(tank.movementProgress, deltaTime, tank.movementSpeed);
        if (isEqual(tank.movementProgress, 1f)) {
            tank.coordinates.set(tank.destinationCoordinates);
        }

        for (int i = 0; i < trees.size(); i++) {
            Tree tree = trees.get(i);
            Graphics treeGfx = treeGraphics.get(i);
            moveRectangleAtTileCenter(((TiledMapTileLayer) level.getLayers().get(0)), treeGfx.boundingBox,
                    tree.coordinates);
        }

        levelRenderer.render();

        batch.begin();

        drawTextureRegionUnscaled(batch, tankGfx.textureRegion, tankGfx.boundingBox, tank.rotation);

        for (Graphics treeGfx : treeGraphics) {
            drawTextureRegionUnscaled(batch, treeGfx.textureRegion, treeGfx.boundingBox, 0f);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        if (graphicsMap != null && graphicsMap.get("tank") != null) {
            graphicsMap.get("tank").dispose();
        }
        if (treeGraphics != null) {
            for (Graphics treeGfx : treeGraphics) {
                treeGfx.dispose();
            }
        }
        if (level != null) {
            level.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}