package ru.mipt.bit.platformer.infrastructure.ai;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.domain.ai.DecisionStrategy;
import ru.mipt.bit.platformer.domain.entities.Direction;
import ru.mipt.bit.platformer.domain.entities.GameCharacter;

public class RecommendationEngineAdapter implements DecisionStrategy {
    private final RecommendationEngineClient client;

    public RecommendationEngineAdapter(RecommendationEngineClient client) {
        this.client = client;
    }

    @Override
    public EnemyDecision recommend(GameCharacter enemy, GameCharacter target) {
        if (enemy == null || target == null) {
            return EnemyDecision.idle();
        }
        GridPoint2 enemyPosition = enemy.coordinates;
        GridPoint2 playerPosition = target.coordinates;
        RecommendationRequest request = new RecommendationRequest(playerPosition.x - enemyPosition.x,
                playerPosition.y - enemyPosition.y);
        RecommendationAction action = client.getRecommendation(request);
        switch (action) {
            case FIRE:
                return EnemyDecision.shoot();
            case MOVE_UP:
                return EnemyDecision.move(Direction.UP);
            case MOVE_DOWN:
                return EnemyDecision.move(Direction.DOWN);
            case MOVE_LEFT:
                return EnemyDecision.move(Direction.LEFT);
            case MOVE_RIGHT:
                return EnemyDecision.move(Direction.RIGHT);
            default:
                return EnemyDecision.idle();
        }
    }
}
