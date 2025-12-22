package ru.mipt.bit.platformer.infrastructure.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

import ru.mipt.bit.platformer.domain.entities.MovableEntity;

public class HealthBarDisplay implements EntityRenderer {
    private final EntityRenderer baseRenderer;
    private final EntityRenderer decoratedRenderer;
    private EntityRenderer currentRenderer;
    private boolean visible;

    public HealthBarDisplay(EntityRenderer baseRenderer, EntityRenderer decoratedRenderer) {
        this.baseRenderer = baseRenderer;
        this.decoratedRenderer = decoratedRenderer;
        this.currentRenderer = baseRenderer;
    }

    public void toggle() {
        setVisible(!visible);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        currentRenderer = visible ? decoratedRenderer : baseRenderer;
    }

    public boolean isVisible() {
        return visible;
    }

    public EntityRenderer getRenderer() {
        return currentRenderer;
    }

    @Override
    public void render(Batch batch, MovableEntity entity, Graphics graphics) {
        currentRenderer.render(batch, entity, graphics);
    }

    @Override
    public void dispose() {
        // Resources are managed by Spring beans (baseRenderer and decoratedRenderer)
    }
}
