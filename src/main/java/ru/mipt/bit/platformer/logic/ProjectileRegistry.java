package ru.mipt.bit.platformer.logic;

import ru.mipt.bit.platformer.classes.Bullet;

public interface ProjectileRegistry {
    boolean register(Bullet bullet);
}
