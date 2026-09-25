package ru.mipt.bit.platformer.infrastructure.entities;

import ru.mipt.bit.platformer.domain.entities.Positionable;

public class Obstacle extends Positionable {
    public Obstacle(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate);
    }
}
