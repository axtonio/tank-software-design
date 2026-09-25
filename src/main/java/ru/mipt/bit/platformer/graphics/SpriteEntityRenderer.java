package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

import ru.mipt.bit.platformer.classes.Graphics;
import ru.mipt.bit.platformer.classes.MovableEntity;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class SpriteEntityRenderer implements EntityRenderer {
    @Override
    public void render(Batch batch, MovableEntity entity, Graphics graphics) {
        drawTextureRegionUnscaled(batch, graphics.graphics, graphics.rectangle, entity.rotation);
    }
}

