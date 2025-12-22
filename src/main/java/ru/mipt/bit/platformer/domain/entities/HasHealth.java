package ru.mipt.bit.platformer.domain.entities;

public interface HasHealth {
    int getMaxHealth();

    int getHealth();

    void setHealth(int health);
}
