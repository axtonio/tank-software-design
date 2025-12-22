package ru.mipt.bit.platformer.classes;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.game.entities.Direction;
import ru.mipt.bit.platformer.game.entities.GameCharacter;
import ru.mipt.bit.platformer.game.entities.ProjectileRegistry;

public class Tank extends GameCharacter {
    private final int maxHealth;
    private int health;
    private ProjectileRegistry projectileRegistry;

    public Tank(int xCooordinate, int yCoordinate, float movementSpeed) {
        super(xCooordinate, yCoordinate, movementSpeed);
        this.maxHealth = 100;
        this.health = 100;
    }

    @Override
    public void setProjectileRegistry(ProjectileRegistry projectileRegistry) {
        this.projectileRegistry = projectileRegistry;
    }

    @Override
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

    @Override
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
