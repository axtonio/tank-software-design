package ru.mipt.bit.platformer.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DirectionTest {

    @Test
    void testDirectionVectors() {
        assertEquals(0, Direction.UP.getVector().x);
        assertEquals(1, Direction.UP.getVector().y);

        assertEquals(-1, Direction.LEFT.getVector().x);
        assertEquals(0, Direction.LEFT.getVector().y);

        assertEquals(0, Direction.DOWN.getVector().x);
        assertEquals(-1, Direction.DOWN.getVector().y);

        assertEquals(1, Direction.RIGHT.getVector().x);
        assertEquals(0, Direction.RIGHT.getVector().y);

        assertEquals(0, Direction.IDLE.getVector().x);
        assertEquals(0, Direction.IDLE.getVector().y);
    }

    @Test
    void testDirectionRotations() {
        assertEquals(90f, Direction.UP.getRotation(), 0.001);
        assertEquals(-180f, Direction.LEFT.getRotation(), 0.001);
        assertEquals(-90f, Direction.DOWN.getRotation(), 0.001);
        assertEquals(0f, Direction.RIGHT.getRotation(), 0.001);
        assertEquals(0f, Direction.IDLE.getRotation(), 0.001);
    }
}