package ru.mipt.bit.platformer.classes;

import static com.badlogic.gdx.math.MathUtils.isEqual;

import com.badlogic.gdx.math.GridPoint2;

public class Tank extends MovableEntity implements HasHealth {
    private final int maxHealth;
    private int health;

    public Tank(int xCooordinate, int yCoordinate, float movementSpeed) {
        super(xCooordinate, yCoordinate, movementSpeed);
        this.maxHealth = 100;
        this.health = 100;
    }

    public void tryMove(Direction dir, GameObject[] obstacles) {
        if (isEqual(movementProgress, 1f)) {
            GridPoint2 targetPosition = getTargetPosition(dir);
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

            if (dir != Direction.IDLE) {
                rotation = dir.rotation;
            }
        }
    }

    private GridPoint2 getTargetPosition(Direction direction) {
        if (direction == Direction.IDLE) {
            return new GridPoint2(coordinates);
        }
        GridPoint2 targetPosition = new GridPoint2(coordinates);
        targetPosition.add(direction.vector);
        return targetPosition;
    }

    public void fire() {
        // Логика стрельбы будет добавлена позже
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        if (health < 0) {
            this.health = 0;
            return;
        }
        if (health > maxHealth) {
            this.health = maxHealth;
            return;
        }
        this.health = health;
    }
}