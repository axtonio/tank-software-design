package ru.mipt.bit.platformer.graphics;

public class HealthBarDisplay {
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
}

