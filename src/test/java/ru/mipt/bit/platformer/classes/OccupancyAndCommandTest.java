package ru.mipt.bit.platformer.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.commands.MoveCommand;
import ru.mipt.bit.platformer.logic.OccupancyManager;

class OccupancyAndCommandTest {

    @Test
    void testBoundsBlocking() {
        OccupancyManager occ = new OccupancyManager(3, 3);
        Tank t = new Tank(0, 0, 0.4f);
        assertFalse(occ.canMove(t, t.coordinates, Direction.LEFT));
        assertFalse(occ.canMove(t, t.coordinates, Direction.DOWN));
        assertTrue(occ.canMove(t, t.coordinates, Direction.UP));
    }

    @Test
    void testObstacleBlocking() {
        OccupancyManager occ = new OccupancyManager(5, 5);
        java.util.HashSet<GridPoint2> obs = new java.util.HashSet<>();
        obs.add(new GridPoint2(2, 1));
        occ.setStaticObstacles(obs);
        Tank t = new Tank(1, 1, 0.4f);
        assertFalse(occ.canMove(t, t.coordinates, Direction.RIGHT));
        assertTrue(occ.canMove(t, t.coordinates, Direction.UP));
    }

    @Test
    void testMoveCommandReservesAndAdvances() {
        OccupancyManager occ = new OccupancyManager(5, 5);
        Tank t = new Tank(1, 1, 0.4f);
        new MoveCommand(t, Direction.RIGHT, occ).execute();
        assertEquals(2, t.destinationCoordinates.x);
        assertEquals(1, t.destinationCoordinates.y);
        assertEquals(0f, t.movementProgress);
    }

    @Test
    void testMoveCommandRotatesWhenBlocked() {
        OccupancyManager occ = new OccupancyManager(5, 5);
        java.util.HashSet<GridPoint2> obs = new java.util.HashSet<>();
        obs.add(new GridPoint2(1, 2));
        occ.setStaticObstacles(obs);
        Tank t = new Tank(1, 1, 0.4f);

        new MoveCommand(t, Direction.UP, occ).execute();

        assertEquals(1, t.destinationCoordinates.x);
        assertEquals(1, t.destinationCoordinates.y);
        assertEquals(Direction.UP.getRotation(), t.rotation);
    }
}
