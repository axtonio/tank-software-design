package ru.mipt.bit.platformer.graphics;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Disposable;

import ru.mipt.bit.platformer.classes.Bullet;
import ru.mipt.bit.platformer.classes.Graphics;
import ru.mipt.bit.platformer.classes.MovableEntity;
import ru.mipt.bit.platformer.classes.Positionable;
import ru.mipt.bit.platformer.classes.Tree;
import ru.mipt.bit.platformer.logic.LevelObserver;

public class GraphicsLevel implements LevelObserver, Disposable {
    private final Graphics tankPrototype;
    private final Graphics treePrototype;
    private final Graphics bulletPrototype;

    private final Map<MovableEntity, Graphics> movableGraphics = new LinkedHashMap<>();
    private final Map<Positionable, Graphics> staticGraphics = new LinkedHashMap<>();

    public GraphicsLevel(Graphics tankPrototype, Graphics treePrototype, Graphics bulletPrototype) {
        this.tankPrototype = tankPrototype;
        this.treePrototype = treePrototype;
        this.bulletPrototype = bulletPrototype;
    }

    @Override
    public void onObjectAdded(Positionable object) {
        if (object instanceof MovableEntity) {
            movableGraphics.put((MovableEntity) object, createMovableGraphics(object));
        } else {
            staticGraphics.put(object, createStaticGraphics(object));
        }
    }

    @Override
    public void onObjectRemoved(Positionable object) {
        if (object instanceof MovableEntity) {
            Graphics graphics = movableGraphics.remove(object);
            if (graphics != null) {
                graphics.dispose();
            }
        } else {
            Graphics graphics = staticGraphics.remove(object);
            if (graphics != null) {
                graphics.dispose();
            }
        }
    }

    public Collection<Map.Entry<MovableEntity, Graphics>> getMovableEntries() {
        return movableGraphics.entrySet();
    }

    public Collection<Map.Entry<Positionable, Graphics>> getStaticEntries() {
        return staticGraphics.entrySet();
    }

    private Graphics createMovableGraphics(Positionable object) {
        if (object instanceof Bullet) {
            return new Graphics(bulletPrototype);
        }
        return new Graphics(tankPrototype);
    }

    private Graphics createStaticGraphics(Positionable object) {
        if (object instanceof Tree) {
            return new Graphics(treePrototype);
        }
        return new Graphics(treePrototype);
    }

    @Override
    public void dispose() {
        tankPrototype.dispose();
        treePrototype.dispose();
        bulletPrototype.dispose();
    }
}
