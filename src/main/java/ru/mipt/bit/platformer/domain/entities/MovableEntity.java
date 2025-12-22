package ru.mipt.bit.platformer.domain.entities;

import com.badlogic.gdx.math.GridPoint2;

public class MovableEntity extends Positionable {
    public GridPoint2 destinationCoordinates;
    public float movementProgress = 1f;
    public float rotation;
    public float movementSpeed;
    private Direction facingDirection = Direction.RIGHT;

    public MovableEntity(int xCooordinate, int yCoordinate, float movementSpeed) {
        super(xCooordinate, yCoordinate);
        this.destinationCoordinates = new GridPoint2(this.coordinates);
        this.movementSpeed = movementSpeed;
        this.rotation = facingDirection.rotation;
    }

    public void setFacingDirection(Direction direction) {
        if (direction == null || direction == Direction.IDLE) {
            return;
        }
        this.facingDirection = direction;
        this.rotation = direction.rotation;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }
}
