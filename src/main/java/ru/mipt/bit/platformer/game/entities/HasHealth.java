package ru.mipt.bit.platformer.game.entities;

public interface HasHealth {
    int getMaxHealth();
    int getHealth();
    void setHealth(int health);
}

