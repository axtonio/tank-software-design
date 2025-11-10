package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

import ru.mipt.bit.platformer.classes.Graphics;
import ru.mipt.bit.platformer.classes.MovableEntity;

public interface EntityRenderer {
    void render(Batch batch, MovableEntity entity, Graphics graphics);
}

