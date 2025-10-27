package ru.mipt.bit.platformer.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
    }

    @Test
    void testTryMoveWithNoObstacles() {
        Tank tank = new Tank(1, 1, 0.4f);
        GameObject[] emptyObstacles = {};

        tank.tryMove(Direction.RIGHT, emptyObstacles);

        assertEquals(2, tank.destinationCoordinates.x);
        assertEquals(1, tank.destinationCoordinates.y);
        assertEquals(0f, tank.movementProgress);
        assertEquals(0f, tank.rotation);
    }

    @Test
    void testTryMoveWithObstacle() {
        Tank tank = new Tank(1, 1, 0.4f);
        Tree tree = new Tree(2, 1); // Преграда справа от танка
        GameObject[] obstacles = { tree };

        tank.tryMove(Direction.RIGHT, obstacles);

        // Танк не должен двигаться, так как путь заблокирован
        assertEquals(1, tank.destinationCoordinates.x);
        assertEquals(1, tank.destinationCoordinates.y);
        assertEquals(1f, tank.movementProgress); // Движение не началось
    }

    @Test
    void testTryMoveToAvailableDirectionWithMultipleObstacles() {
        Tank tank = new Tank(1, 1, 0.4f);
        Tree tree1 = new Tree(2, 1); // Преграда справа
        Tree tree2 = new Tree(0, 1); // Преграда слева
        GameObject[] obstacles = { tree1, tree2 };

        tank.tryMove(Direction.UP, obstacles);

        // Танк должен двигаться вверх, так как этот путь свободен
        assertEquals(1, tank.destinationCoordinates.x);
        assertEquals(2, tank.destinationCoordinates.y);
        assertEquals(0f, tank.movementProgress);
    }
}