package ru.mipt.bit.platformer.classes;

public abstract class GameObject extends Positionable {

    public GameObject(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate);
    }
}