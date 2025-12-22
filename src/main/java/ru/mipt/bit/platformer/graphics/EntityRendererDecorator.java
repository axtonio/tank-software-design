package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

import ru.mipt.bit.platformer.classes.Graphics;
import ru.mipt.bit.platformer.game.entities.MovableEntity;

public abstract class EntityRendererDecorator implements EntityRenderer {
    protected final EntityRenderer delegate;

    protected EntityRendererDecorator(EntityRenderer delegate) {
        this.delegate = delegate;
    }

    @Override
    public void render(Batch batch, MovableEntity entity, Graphics graphics) {
        delegate.render(batch, entity, graphics);
    }
}
