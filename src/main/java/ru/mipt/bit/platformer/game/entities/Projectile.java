package ru.mipt.bit.platformer.game.entities;

import com.badlogic.gdx.math.GridPoint2;

public abstract class Projectile extends MovableEntity {
    private final Direction direction;
    private final GameCharacter owner;
    private final int damage;

    public Projectile(int x, int y, float speed, Direction direction, GameCharacter owner, int damage) {
        super(x, y, speed);
        this.direction = direction;
        this.owner = owner;
        this.damage = damage;
        this.rotation = direction.rotation;
    }

    public Direction getDirection() {
        return direction;
    }

    public GameCharacter getOwner() {
        return owner;
    }

    public int getDamage() {
        return damage;
    }

    public GridPoint2 peekNextStep() {
        return new GridPoint2(coordinates).add(direction.vector);
    }
}

