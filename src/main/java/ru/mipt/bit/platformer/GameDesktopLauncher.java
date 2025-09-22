package ru.mipt.bit.platformer;

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
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.classes.Tank;
import ru.mipt.bit.platformer.classes.GameObject;
import ru.mipt.bit.platformer.classes.Tree;

import static com.badlogic.gdx.Input.Keys.*;
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

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        tank = new Tank("images/tank_blue.png", 1, 1);
        tree = new Tree("images/greenTree.png", 1, 3);
        moveRectangleAtTileCenter(groundLayer, tree.boundingBox, tree.coordinates);
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
            tank.tryMove(Tank.Direction.UP, tree);
        }
        if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) {
            tank.tryMove(Tank.Direction.LEFT, tree);
        }
        if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
            tank.tryMove(Tank.Direction.DOWN, tree);
        }
        if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) {
            tank.tryMove(Tank.Direction.RIGHT, tree);
        }

        // calculate interpolated player screen coordinates
        tileMovement.moveRectangleBetweenTileCenters(tank.boundingBox, tank.coordinates, tank.destinationCoordinates, tank.movementProgress);

        tank.movementProgress = continueProgress(tank.movementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(tank.movementProgress, 1f)) {
            // record that the player has reached his/her destination
            tank.coordinates.set(tank.destinationCoordinates);
        }

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();

        // Универсальный рендеринг игровых объектов
        renderGameObject(tank, tank.rotation);
        renderGameObject(tree, 0f);

        // submit all drawing requests
        batch.end();
    }

    private void renderGameObject(GameObject gameObject, float rotation) {
        drawTextureRegionUnscaled(batch, gameObject.graphics, gameObject.boundingBox, rotation);
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
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        tree.dispose();
        tank.dispose();
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
