package ru.mipt.bit.platformer.config;

import ru.mipt.bit.platformer.infrastructure.entities.Bullet;
import ru.mipt.bit.platformer.infrastructure.entities.Tank;
import ru.mipt.bit.platformer.infrastructure.entities.Tree;
import ru.mipt.bit.platformer.infrastructure.graphics.Graphics;
import ru.mipt.bit.platformer.infrastructure.graphics.GraphicsLevel;

public class GraphicsConfigurator {
    public void configure(GraphicsLevel graphicsLevel) {
        graphicsLevel.addPrototype(Tank.class, new Graphics("images/tank_blue.png"));
        graphicsLevel.addPrototype(Tree.class, new Graphics("images/greenTree.png"));
        graphicsLevel.addPrototype(Bullet.class, new Graphics("images/bullet.png"));
    }
}
