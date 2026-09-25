package ru.mipt.bit.platformer.classes;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank extends GameObject {
    public GridPoint2 destinationCoordinates;
    public float movementProgress = 1f;
    public float rotation;
    public float movementSpeed;
    private Direction currentDirection;
    
    public Tank(int xCoordinate, int yCoordinate, float movementSpeed) {
        super(xCoordinate, yCoordinate);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.rotation = 0f;
        this.currentDirection = Direction.RIGHT;
        this.movementSpeed = movementSpeed;
    }
    
    public void tryMove(Direction direction, GameObject[] obstacles) {
        if (isEqual(movementProgress, 1f)) {
            GridPoint2 targetPosition = getTargetPosition(direction);
            boolean canMove = true;
            
            for (GameObject obstacle : obstacles) {
                if (targetPosition.equals(obstacle.coordinates)) {
                    canMove = false;
                    break;
                }
            }
            
            if (canMove) {
                destinationCoordinates.set(targetPosition);
                movementProgress = 0f;
            }
            
            if (direction != Direction.IDLE) {
                rotation = direction.getRotation();
                currentDirection = direction;
            }
        }
    }
    
    private GridPoint2 getTargetPosition(Direction direction) {
        if (direction == Direction.IDLE) {
            return new GridPoint2(coordinates);
        }
        GridPoint2 targetPosition = new GridPoint2(coordinates);
        targetPosition.add(direction.getVector());
        return targetPosition;
    }
    
    public void fire() {
        // Логика стрельбы будет добавлена позже
    }
}