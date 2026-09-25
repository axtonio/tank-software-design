package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.classes.Direction;

public interface InputController {
    Direction getDirection();

    boolean isFireButtonPressed();
}
