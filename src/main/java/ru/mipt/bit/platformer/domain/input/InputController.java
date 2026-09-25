package ru.mipt.bit.platformer.domain.input;

import ru.mipt.bit.platformer.domain.entities.Direction;

public interface InputController {
    Direction getDirection();

    boolean isFireButtonPressed();
}
