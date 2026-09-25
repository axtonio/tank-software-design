package ru.mipt.bit.platformer.classes;

import com.badlogic.gdx.math.GridPoint2;

public class ObjectDirection {
    public Direction dir;
    private KeyboardListener kl;

    public ObjectDirection() {
        this.dir = Direction.UP;
        this.kl = new KeyboardListener();
    }

    public GridPoint2 getDirectionVector() {
        return new GridPoint2(dir.vector);
    }

    public float getRotation() {
        return dir.rotation;
    }

    public void update() {
        dir = kl.captureMovementKey();
    }
}
