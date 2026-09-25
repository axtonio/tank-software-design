package ru.mipt.bit.platformer.domain.ai;

import ru.mipt.bit.platformer.domain.entities.Direction;

public interface Decision {
    boolean shouldShoot();

    Direction getDirection();
}
