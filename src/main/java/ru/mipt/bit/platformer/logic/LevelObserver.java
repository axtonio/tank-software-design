package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.classes.Positionable;

public interface LevelObserver {
    void onObjectAdded(Positionable object);

    void onObjectRemoved(Positionable object);
}
