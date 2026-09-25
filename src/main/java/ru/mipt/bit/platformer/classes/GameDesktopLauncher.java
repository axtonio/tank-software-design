package ru.mipt.bit.platformer.classes;

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
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.classes.Tank;
import ru.mipt.bit.platformer.classes.Tree;
import ru.mipt.bit.platformer.classes.GameObject;
import ru.mipt.bit.platformer.classes.Graphics;
import ru.mipt.bit.platformer.classes.KeyboardListener;
import ru.mipt.bit.platformer.classes.Direction;
import ru.mipt.bit.platformer.input.InputController;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private Tank tank;
    private Tree tree;
    private Graphics tankGraphics;
    private Graphics treeGraphics;
    private InputController inputController;

    @Override
    public void create() {
        batch = new SpriteBatch();
        inputController = new KeyboardListener();

        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        tank = new Tank(1, 1, MOVEMENT_SPEED);
        tree = new Tree(1, 3);

        tankGraphics = new Graphics("images/tank_blue.png");
        treeGraphics = new Graphics("images/greenTree.png");

        moveRectangleAtTileCenter(groundLayer, treeGraphics.boundingBox, tree.coordinates);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        Direction direction = inputController.getDirection();
        GameObject[] obstacles = {tree};

        tank.tryMove(direction, obstacles);

        if (inputController.isFireButtonPressed()) {
            tank.fire();
        }

        tileMovement.moveRectangleBetweenTileCenters(tankGraphics.boundingBox, tank.coordinates, tank.destinationCoordinates, tank.movementProgress);

        tank.movementProgress = continueProgress(tank.movementProgress, deltaTime, tank.movementSpeed);
        if (isEqual(tank.movementProgress, 1f)) {
            tank.coordinates.set(tank.destinationCoordinates);
        }

        levelRenderer.render();

        batch.begin();

        drawTextureRegionUnscaled(batch, tankGraphics.textureRegion, tankGraphics.boundingBox, tank.rotation);
        drawTextureRegionUnscaled(batch, treeGraphics.textureRegion, treeGraphics.boundingBox, 0f);

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
        tankGraphics.dispose();
        treeGraphics.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}