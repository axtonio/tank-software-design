package ru.mipt.bit.platformer.game.level;

public interface LevelObservable {
    void addObserver(LevelObserver observer);

    void removeObserver(LevelObserver observer);
}

