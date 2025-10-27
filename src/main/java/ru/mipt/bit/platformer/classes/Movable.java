package ru.mipt.bit.platformer.classes;

import java.util.ArrayList;

public interface Movable {
    public void moveSelfInCurrentDirection(ArrayList<? extends Positionable> obstacles);

    public void updateDirection();
}
