package ru.mipt.bit.platformer.domain.level;

import ru.mipt.bit.platformer.domain.entities.Positionable;

public interface LevelObserver {
    void onObjectAdded(Positionable object);

    void onObjectRemoved(Positionable object);
}
