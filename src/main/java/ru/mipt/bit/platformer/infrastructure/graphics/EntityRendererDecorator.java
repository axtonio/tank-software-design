package ru.mipt.bit.platformer.infrastructure.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

import ru.mipt.bit.platformer.domain.entities.MovableEntity;

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
