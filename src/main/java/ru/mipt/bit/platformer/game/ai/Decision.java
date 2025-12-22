package ru.mipt.bit.platformer.game.ai;
import ru.mipt.bit.platformer.game.entities.Direction;

public interface Decision {
    boolean shouldShoot();

    Direction getDirection();
}
