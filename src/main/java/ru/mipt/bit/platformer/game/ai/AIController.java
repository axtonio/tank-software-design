package ru.mipt.bit.platformer.game.ai;
import ru.mipt.bit.platformer.game.entities.Direction;

public interface AIController {
    Direction chooseDirection();

    boolean shouldShoot();
}
