package ru.mipt.bit.platformer.classes;

import com.badlogic.gdx.math.GridPoint2;

public abstract class GameObject {
    public GridPoint2 coordinates;
    
    public GameObject(int xCoordinate, int yCoordinate) {
        this.coordinates = new GridPoint2(xCoordinate, yCoordinate);
    }
}