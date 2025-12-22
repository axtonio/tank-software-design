package ru.mipt.bit.platformer.game.input;
import ru.mipt.bit.platformer.game.entities.Direction;

public interface InputController {
    Direction getDirection();

    boolean isFireButtonPressed();
}
