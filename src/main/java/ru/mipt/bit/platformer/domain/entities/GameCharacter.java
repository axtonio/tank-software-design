package ru.mipt.bit.platformer.domain.entities;

public abstract class GameCharacter extends MovableEntity implements HasHealth {

    public GameCharacter(int xCooordinate, int yCoordinate, float movementSpeed) {
        super(xCooordinate, yCoordinate, movementSpeed);
    }

    public abstract void fire();

    public abstract void setProjectileRegistry(ProjectileRegistry projectileRegistry);

    public abstract void takeDamage(int damage);
}
