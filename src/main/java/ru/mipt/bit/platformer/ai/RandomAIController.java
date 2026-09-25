package ru.mipt.bit.platformer.ai;

import java.util.Random;

import ru.mipt.bit.platformer.classes.Direction;

public class RandomAIController {
    private final Random random = new Random();

    public Direction chooseDirection() {
        int r = random.nextInt(4);
        switch (r) {
            case 0:
                return Direction.UP;
            case 1:
                return Direction.DOWN;
            case 2:
                return Direction.LEFT;
            default:
                return Direction.RIGHT;
        }
    }
}


