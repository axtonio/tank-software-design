package ru.mipt.bit.platformer.classes;

import static com.badlogic.gdx.math.MathUtils.isEqual;

import java.util.ArrayList;

import com.badlogic.gdx.math.GridPoint2;

public class MovableEntity extends Positionable implements Movable {
    public GridPoint2 destinationCoordinates;
    public float movementProgress = 1f;
    public float rotation;
    public ObjectDirection direction;
    public float movementSpeed = 0f;
    private Direction facingDirection = Direction.RIGHT;

    public MovableEntity(int xCooordinate, int yCoordinate, float movementSpeed) {
        super(xCooordinate, yCoordinate);

        // set initial position
        this.destinationCoordinates = new GridPoint2(this.coordinates);
        this.rotation = facingDirection.rotation;
        this.direction = new ObjectDirection();
        this.movementSpeed = movementSpeed;
    }

    private boolean isMovementActionCompleted() {
        return isEqual(movementProgress, 1f);
    }

    private boolean collidesWithObstacle(Positionable obstacle) {
        return obstacle.coordinates.equals(coordinates.cpy().add(direction.getDirectionVector()));
    }

    public void tryGo(ArrayList<? extends Positionable> obstacles) {
        if (isMovementActionCompleted()) {
            boolean noCollision = true;
            for (Positionable obstacle : obstacles) {
                if (collidesWithObstacle(obstacle)) {
                    noCollision = false;
                    break;
                }
            }
            if (noCollision) {
                destinationCoordinates.add(direction.getDirectionVector());
                movementProgress = 0f;
            }
            if (direction.dir != Direction.IDLE) {
                rotation = direction.getRotation();
            }
        }
    }

    public void updateDirection() {
        this.direction.update();
    }

    public void moveSelfInCurrentDirection(ArrayList<? extends Positionable> obstacles) {
        tryGo(obstacles);
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
