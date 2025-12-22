package ru.mipt.bit.platformer.game.entities;

public abstract class GameObject extends Positionable {

    public GameObject(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate);
    }
}

