package ru.mipt.bit.platformer.domain.ai;

import ru.mipt.bit.platformer.domain.entities.Direction;

public interface AIController {
    Direction chooseDirection();

    boolean shouldShoot();
}
