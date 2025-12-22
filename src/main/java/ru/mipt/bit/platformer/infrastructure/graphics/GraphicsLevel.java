package ru.mipt.bit.platformer.infrastructure.graphics;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Disposable;

import ru.mipt.bit.platformer.domain.entities.MovableEntity;
import ru.mipt.bit.platformer.domain.entities.Positionable;
import ru.mipt.bit.platformer.domain.level.LevelObserver;

public class GraphicsLevel implements LevelObserver, Disposable {
    private final Map<Class<?>, Graphics> prototypes = new HashMap<>();
    private final Map<MovableEntity, Graphics> movableGraphics = new LinkedHashMap<>();
    private final Map<Positionable, Graphics> staticGraphics = new LinkedHashMap<>();

    public void addPrototype(Class<?> clazz, Graphics graphics) {
        prototypes.put(clazz, graphics);
    }

    @Override
    public void onObjectAdded(Positionable object) {
        if (object instanceof MovableEntity) {
            movableGraphics.put((MovableEntity) object, createGraphics(object));
        } else {
            staticGraphics.put(object, createGraphics(object));
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

    private Graphics createGraphics(Positionable object) {
        Graphics prototype = prototypes.get(object.getClass());
        if (prototype == null) {
            throw new IllegalArgumentException("No prototype found for " + object.getClass());
        }
        return new Graphics(prototype);
    }

    @Override
    public void dispose() {
        for (Graphics prototype : prototypes.values()) {
            prototype.dispose();
        }
    }
}
