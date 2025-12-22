package ru.mipt.bit.platformer.config;

import ru.mipt.bit.platformer.classes.Bullet;
import ru.mipt.bit.platformer.classes.Graphics;
import ru.mipt.bit.platformer.classes.Tank;
import ru.mipt.bit.platformer.classes.Tree;
import ru.mipt.bit.platformer.graphics.GraphicsLevel;

public class GraphicsConfigurator {
    public void configure(GraphicsLevel graphicsLevel) {
        graphicsLevel.addPrototype(Tank.class, new Graphics("images/tank_blue.png"));
        graphicsLevel.addPrototype(Tree.class, new Graphics("images/greenTree.png"));
        graphicsLevel.addPrototype(Bullet.class, new Graphics("images/bullet.png"));
    }
}
