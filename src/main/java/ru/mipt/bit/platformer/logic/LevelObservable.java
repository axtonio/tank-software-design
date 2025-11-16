package ru.mipt.bit.platformer.logic;

public interface LevelObservable {
    void addObserver(LevelObserver observer);

    void removeObserver(LevelObserver observer);
}

