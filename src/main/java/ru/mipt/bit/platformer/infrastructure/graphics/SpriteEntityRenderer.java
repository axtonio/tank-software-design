package ru.mipt.bit.platformer.infrastructure.graphics;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

import com.badlogic.gdx.graphics.g2d.Batch;

import ru.mipt.bit.platformer.domain.entities.MovableEntity;

public class SpriteEntityRenderer implements EntityRenderer {
    @Override
    public void render(Batch batch, MovableEntity entity, Graphics graphics) {
        drawTextureRegionUnscaled(batch, graphics.graphics, graphics.rectangle, entity.rotation);
    }

    @Override
    public void dispose() {
        // No resources to dispose
    }
}
