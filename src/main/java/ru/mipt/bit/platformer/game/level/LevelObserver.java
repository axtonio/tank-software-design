package ru.mipt.bit.platformer.game.level;
import ru.mipt.bit.platformer.game.entities.Positionable;

public interface LevelObserver {
    void onObjectAdded(Positionable object);

    void onObjectRemoved(Positionable object);
}
