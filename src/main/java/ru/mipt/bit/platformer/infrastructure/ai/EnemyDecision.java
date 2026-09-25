package ru.mipt.bit.platformer.infrastructure.ai;

import ru.mipt.bit.platformer.domain.ai.Decision;
import ru.mipt.bit.platformer.domain.entities.Direction;

public class EnemyDecision implements Decision {
    private final boolean shouldShoot;
    private final Direction direction;

    private EnemyDecision(boolean shouldShoot, Direction direction) {
        this.shouldShoot = shouldShoot;
        this.direction = direction;
    }

    public static EnemyDecision shoot() {
        return new EnemyDecision(true, Direction.IDLE);
    }

    public static EnemyDecision move(Direction direction) {
        return new EnemyDecision(false, direction);
    }

    public static EnemyDecision idle() {
        return new EnemyDecision(false, Direction.IDLE);
    }

    @Override
    public boolean shouldShoot() {
        return shouldShoot;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
