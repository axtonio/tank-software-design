package ru.mipt.bit.platformer.ai;

import java.util.Random;

import ru.mipt.bit.platformer.game.entities.Direction;
import ru.mipt.bit.platformer.game.ai.AIController;

public class RandomAIController implements AIController {
    private final Random random = new Random();
    private final float shootProbability;

    public RandomAIController() {
        this(0.15f);
    }

    public RandomAIController(float shootProbability) {
        this.shootProbability = Math.max(0f, Math.min(1f, shootProbability));
    }

    @Override
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

    @Override
    public boolean shouldShoot() {
        return random.nextFloat() < shootProbability;
    }
}
