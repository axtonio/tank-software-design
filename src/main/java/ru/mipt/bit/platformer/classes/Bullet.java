package ru.mipt.bit.platformer.classes;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.game.entities.Direction;
import ru.mipt.bit.platformer.game.entities.GameCharacter;
import ru.mipt.bit.platformer.game.entities.Projectile;

public class Bullet extends Projectile {
    public static final float DEFAULT_SPEED = 0.15f;
    public static final int DEFAULT_DAMAGE = 25;

    public Bullet(GridPoint2 startPosition, Direction direction, GameCharacter owner) {
        this(startPosition, direction, owner, DEFAULT_SPEED, DEFAULT_DAMAGE);
    }

    public Bullet(GridPoint2 startPosition, Direction direction, GameCharacter owner, float speed, int damage) {
        super(startPosition.x, startPosition.y, speed, direction, owner, damage);
    }
}
