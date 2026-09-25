package ru.mipt.bit.platformer.classes;

import static com.badlogic.gdx.math.MathUtils.isEqual;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.logic.ProjectileRegistry;

public class Tank extends MovableEntity implements HasHealth {
    private final int maxHealth;
    private int health;
    private ProjectileRegistry projectileRegistry;

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
                setFacingDirection(dir);
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

    public void setProjectileRegistry(ProjectileRegistry projectileRegistry) {
        this.projectileRegistry = projectileRegistry;
    }

    public void fire() {
        if (projectileRegistry == null) {
            return;
        }
        Direction direction = getFacingDirection();
        if (direction == null || direction == Direction.IDLE) {
            direction = Direction.UP;
        }
        GridPoint2 spawnPoint = new GridPoint2(coordinates).add(direction.vector);
        Bullet bullet = new Bullet(spawnPoint, direction, this);
        projectileRegistry.register(bullet);
    }

    public void takeDamage(int damage) {
        if (damage <= 0) {
            return;
        }
        setHealth(health - damage);
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