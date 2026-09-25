package ru.mipt.bit.platformer.domain.level;

import com.badlogic.gdx.math.GridPoint2;

public interface LevelMap {
    void registerPosition(Object actorKey, GridPoint2 position);

    void commitPosition(Object actorKey, GridPoint2 position);

    void release(Object actorKey);

    void updateReservation(Object actorKey, GridPoint2 from, GridPoint2 to);

    boolean isWithinBounds(GridPoint2 position);

    boolean isStaticObstacle(GridPoint2 position);

    void removeStaticObstacle(GridPoint2 position);

    void unregisterPosition(Object actorKey);
}
