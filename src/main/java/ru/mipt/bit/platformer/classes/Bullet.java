package ru.mipt.bit.platformer.classes;

import com.badlogic.gdx.math.GridPoint2;

public class Bullet extends MovableEntity {
    public static final float DEFAULT_SPEED = 0.15f;
    public static final int DEFAULT_DAMAGE = 25;

    private final Direction direction;
    private final Tank owner;
    private final int damage;

    public Bullet(GridPoint2 startPosition, Direction direction, Tank owner) {
        this(startPosition, direction, owner, DEFAULT_SPEED, DEFAULT_DAMAGE);
    }

    public Bullet(GridPoint2 startPosition, Direction direction, Tank owner, float speed, int damage) {
        super(startPosition.x, startPosition.y, speed);
        this.direction = direction;
        this.owner = owner;
        this.damage = damage;
        this.rotation = direction.rotation;
    }

    public Direction getDirection() {
        return direction;
    }

    public Tank getOwner() {
        return owner;
    }

    public int getDamage() {
        return damage;
    }

    public GridPoint2 peekNextStep() {
        return new GridPoint2(coordinates).add(direction.vector);
    }
}

