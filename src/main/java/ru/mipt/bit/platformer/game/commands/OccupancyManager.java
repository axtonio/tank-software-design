package ru.mipt.bit.platformer.game.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.game.entities.Direction;

public class OccupancyManager {
    private final int mapWidth;
    private final int mapHeight;

    private final Set<GridPoint2> staticObstacles = new HashSet<>();
    private final Map<Object, MoveReservation> reservations = new HashMap<>();
    private final Map<Object, GridPoint2> occupiedPositions = new HashMap<>();

    public OccupancyManager(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void setStaticObstacles(Set<GridPoint2> obstacles) {
        staticObstacles.clear();
        staticObstacles.addAll(obstacles);
    }

    public boolean isWithinBounds(GridPoint2 position) {
        return position.x >= 0 && position.x < mapWidth && position.y >= 0 && position.y < mapHeight;
    }

    public boolean canMove(Object actorKey, GridPoint2 from, Direction direction) {
        GridPoint2 to = new GridPoint2(from).add(direction.vector);
        if (!isWithinBounds(to)) {
            return false;
        }
        if (staticObstacles.contains(to)) {
            return false;
        }
        for (Map.Entry<Object, GridPoint2> entry : occupiedPositions.entrySet()) {
            if (entry.getKey() == actorKey) {
                continue;
            }
            if (entry.getValue().equals(to)) {
                return false;
            }
        }
        for (Map.Entry<Object, MoveReservation> entry : reservations.entrySet()) {
            if (entry.getKey() == actorKey) {
                continue;
            }
            MoveReservation r = entry.getValue();
            if (r.from.equals(to) || r.to.equals(to) || r.to.equals(from)) {
                return false;
            }
        }
        return true;
    }

    public void reserve(Object actorKey, GridPoint2 from, GridPoint2 to) {
        reservations.put(actorKey, new MoveReservation(new GridPoint2(from), new GridPoint2(to)));
    }

    public boolean isStaticObstacle(GridPoint2 position) {
        return staticObstacles.contains(position);
    }

    public void removeStaticObstacle(GridPoint2 position) {
        staticObstacles.remove(position);
    }

    public void release(Object actorKey) {
        reservations.remove(actorKey);
    }

    public void updateReservation(Object actorKey, GridPoint2 from, GridPoint2 to) {
        reservations.put(actorKey, new MoveReservation(new GridPoint2(from), new GridPoint2(to)));
    }

    public void registerPosition(Object actorKey, GridPoint2 position) {
        occupiedPositions.put(actorKey, new GridPoint2(position));
    }

    public void commitPosition(Object actorKey, GridPoint2 position) {
        registerPosition(actorKey, position);
    }

    public void unregisterPosition(Object actorKey) {
        occupiedPositions.remove(actorKey);
    }

    private static class MoveReservation {
        final GridPoint2 from;
        final GridPoint2 to;

        MoveReservation(GridPoint2 from, GridPoint2 to) {
            this.from = from;
            this.to = to;
        }
    }
}
