package ru.mipt.bit.platformer.classes;

import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    UP(new GridPoint2(0, 1), 90f),
    RIGHT(new GridPoint2(1, 0), 0f),
    DOWN(new GridPoint2(0, -1), -90f),
    LEFT(new GridPoint2(-1, 0), -180f),
    IDLE(new GridPoint2(0, 0), 0f);

    private final GridPoint2 vector;
    private final float rotation;

    Direction(GridPoint2 vector, float rotation) {
        this.vector = vector;
        this.rotation = rotation;
    }

    public GridPoint2 getVector() {
        return new GridPoint2(vector);
    }

    public float getRotation() {
        return rotation;
    }
}