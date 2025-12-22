package ru.mipt.bit.platformer.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.commands.MoveCommand;
import ru.mipt.bit.platformer.game.commands.OccupancyManager;
import ru.mipt.bit.platformer.game.entities.Direction;

class TankTest {

    @Test
    void testTankInitialization() {
        Tank tank = new Tank(5, 3, 0.5f);

        assertEquals(5, tank.coordinates.x);
        assertEquals(3, tank.coordinates.y);
        assertEquals(5, tank.destinationCoordinates.x);
        assertEquals(3, tank.destinationCoordinates.y);
        assertEquals(1f, tank.movementProgress);
        assertEquals(0f, tank.rotation);
        assertEquals(0.5f, tank.movementSpeed);
        assertEquals(100, tank.getMaxHealth());
        assertEquals(100, tank.getHealth());
    }

    @Test
    void testTryMoveWithNoObstacles() {
        Tank tank = new Tank(1, 1, 0.4f);
        OccupancyManager occupancy = new OccupancyManager(10, 10);
        MoveCommand command = new MoveCommand(tank, Direction.RIGHT, occupancy);

        command.execute();

        assertEquals(2, tank.destinationCoordinates.x);
        assertEquals(1, tank.destinationCoordinates.y);
        assertEquals(0f, tank.movementProgress);
        assertEquals(0f, tank.rotation);
    }

    @Test
    void testTryMoveWithObstacle() {
        Tank tank = new Tank(1, 1, 0.4f);
        OccupancyManager occupancy = new OccupancyManager(10, 10);
        Tree tree = new Tree(2, 1);
        java.util.HashSet<GridPoint2> staticObs = new java.util.HashSet<>();
        staticObs.add(tree.coordinates);
        occupancy.setStaticObstacles(staticObs);

        MoveCommand command = new MoveCommand(tank, Direction.RIGHT, occupancy);
        command.execute();

        // Танк не должен двигаться, так как путь заблокирован
        assertEquals(1, tank.destinationCoordinates.x);
        assertEquals(1, tank.destinationCoordinates.y);
        assertEquals(1f, tank.movementProgress); // Движение не началось
    }

    @Test
    void testSetHealthBounds() {
        Tank tank = new Tank(0, 0, 0.4f);
        tank.setHealth(150);
        assertEquals(100, tank.getHealth());
        tank.setHealth(-10);
        assertEquals(0, tank.getHealth());
    }

    @Test
    void testTryMoveToAvailableDirectionWithMultipleObstacles() {
        Tank tank = new Tank(1, 1, 0.4f);
        OccupancyManager occupancy = new OccupancyManager(10, 10);

        Tree tree1 = new Tree(2, 1); // Преграда справа
        Tree tree2 = new Tree(0, 1); // Преграда слева
        java.util.HashSet<GridPoint2> staticObs = new java.util.HashSet<>();
        staticObs.add(tree1.coordinates);
        staticObs.add(tree2.coordinates);
        occupancy.setStaticObstacles(staticObs);

        MoveCommand command = new MoveCommand(tank, Direction.UP, occupancy);
        command.execute();

        // Танк должен двигаться вверх, так как этот путь свободен
        assertEquals(1, tank.destinationCoordinates.x);
        assertEquals(2, tank.destinationCoordinates.y);
        assertEquals(0f, tank.movementProgress);
    }
}
