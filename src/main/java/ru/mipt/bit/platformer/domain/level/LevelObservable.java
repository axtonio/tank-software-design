package ru.mipt.bit.platformer.domain.level;

public interface LevelObservable {
    void addObserver(LevelObserver observer);

    void removeObserver(LevelObserver observer);
}
