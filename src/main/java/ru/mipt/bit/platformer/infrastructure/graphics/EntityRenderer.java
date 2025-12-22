package ru.mipt.bit.platformer.infrastructure.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

import ru.mipt.bit.platformer.domain.entities.MovableEntity;

public interface EntityRenderer extends Disposable {
    void render(Batch batch, MovableEntity entity, Graphics graphics);
}
