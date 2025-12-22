package ru.mipt.bit.platformer.infrastructure.graphics;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import ru.mipt.bit.platformer.domain.entities.MovableEntity;
import ru.mipt.bit.platformer.domain.entities.Positionable;
import ru.mipt.bit.platformer.util.TileMovement;

public class WorldRenderer {
    private final GraphicsLevel graphicsLevel;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;
    private final Batch batch;
    private final MapRenderer levelRenderer;
    private final EntityRenderer healthBarRenderer;

    public WorldRenderer(GraphicsLevel graphicsLevel, TiledMapTileLayer groundLayer, TileMovement tileMovement,
            Batch batch, MapRenderer levelRenderer, EntityRenderer healthBarRenderer) {
        this.graphicsLevel = graphicsLevel;
        this.groundLayer = groundLayer;
        this.tileMovement = tileMovement;
        this.batch = batch;
        this.levelRenderer = levelRenderer;
        this.healthBarRenderer = healthBarRenderer;
    }

    public void render() {
        glClearScreen();
        levelRenderer.render();
        batch.begin();

        renderMovableEntities(healthBarRenderer);
        renderStaticObjects();

        batch.end();
    }

    private void glClearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
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
}
