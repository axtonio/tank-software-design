package ru.mipt.bit.platformer.classes;

import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    IDLE(new GridPoint2(0, 0), 0f),
    LEFT(new GridPoint2(-1, 0), -180f),
    UP(new GridPoint2(0, 1), 90f),
    RIGHT(new GridPoint2(1, 0), 0f),
    DOWN(new GridPoint2(0, -1), -90f);

    public final GridPoint2 vector;
    public final float rotation;

    Direction(GridPoint2 vector, float rotation) {
        this.vector = vector;
        this.rotation = rotation;
    }

    // Legacy methods for backward compatibility with tests
    public GridPoint2 getVector() {
        return new GridPoint2(vector);
    }

    public float getRotation() {
        return rotation;
    }
}