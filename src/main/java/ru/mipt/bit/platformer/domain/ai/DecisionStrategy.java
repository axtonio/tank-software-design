package ru.mipt.bit.platformer.domain.ai;

import ru.mipt.bit.platformer.domain.entities.GameCharacter;

public interface DecisionStrategy {
    Decision recommend(GameCharacter enemy, GameCharacter target);
}
