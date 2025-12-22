package ru.mipt.bit.platformer.infrastructure.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import ru.mipt.bit.platformer.domain.entities.HasHealth;
import ru.mipt.bit.platformer.domain.entities.MovableEntity;

public class HealthBarEntityRenderer extends EntityRendererDecorator {
    private final Texture pixelTexture;
    private final float heightRatio;
    private final float verticalOffset;

    public HealthBarEntityRenderer(EntityRenderer delegate) {
        this(delegate, 0.1f, 6f);
    }

    public HealthBarEntityRenderer(EntityRenderer delegate, float heightRatio, float verticalOffset) {
        super(delegate);
        this.heightRatio = heightRatio;
        this.verticalOffset = verticalOffset;
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixelTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void render(Batch batch, MovableEntity entity, Graphics graphics) {
        super.render(batch, entity, graphics);
        if (!(entity instanceof HasHealth)) {
            return;
        }
        HasHealth hasHealth = (HasHealth) entity;
        int maxHealth = hasHealth.getMaxHealth();
        if (maxHealth <= 0) {
            return;
        }
        float percent = Math.max(0f, Math.min(1f, (float) hasHealth.getHealth() / maxHealth));
        float barWidth = graphics.rectangle.getWidth();
        float barHeight = graphics.rectangle.getHeight() * heightRatio;
        float x = graphics.rectangle.x;
        float y = graphics.rectangle.y + graphics.rectangle.getHeight() + verticalOffset;
        Color previous = new Color(batch.getColor());
        batch.setColor(Color.DARK_GRAY);
        batch.draw(pixelTexture, x, y, barWidth, barHeight);
        batch.setColor(percent > 0.3f ? Color.GREEN : Color.RED);
        batch.draw(pixelTexture, x, y, barWidth * percent, barHeight);
        batch.setColor(previous);
    }

    @Override
    public void dispose() {
        pixelTexture.dispose();
    }
}
