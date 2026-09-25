package ru.mipt.bit.platformer.ai;

import java.util.Random;

import ru.mipt.bit.platformer.classes.Direction;

public class RandomAIController {
    private final Random random = new Random();
    private final float shootProbability;

    public RandomAIController() {
        this(0.15f);
    }

    public RandomAIController(float shootProbability) {
        this.shootProbability = Math.max(0f, Math.min(1f, shootProbability));
    }

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

    public boolean shouldShoot() {
        return random.nextFloat() < shootProbability;
    }
}


