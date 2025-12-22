package ru.mipt.bit.platformer.game.ai;
import ru.mipt.bit.platformer.game.entities.GameCharacter;

public interface DecisionStrategy {
    Decision recommend(GameCharacter enemy, GameCharacter target);
}
