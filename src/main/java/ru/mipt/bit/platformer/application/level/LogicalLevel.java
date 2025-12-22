package ru.mipt.bit.platformer.application.level;

import java.util.List;

import ru.mipt.bit.platformer.application.commands.Command;
import ru.mipt.bit.platformer.application.commands.CommandsFactory;
import ru.mipt.bit.platformer.domain.ai.AIController;
import ru.mipt.bit.platformer.domain.ai.Decision;
import ru.mipt.bit.platformer.domain.ai.DecisionStrategy;
import ru.mipt.bit.platformer.domain.entities.Direction;
import ru.mipt.bit.platformer.domain.entities.GameCharacter;
import ru.mipt.bit.platformer.domain.entities.GameObject;
import ru.mipt.bit.platformer.domain.level.GameRules;

public class LogicalLevel extends GameRules {
    private final List<AIController> enemyAI;
    private final DecisionStrategy decisionStrategy;
    private final CommandsFactory commandsFactory;
    private final OccupancyManager occupancyManager;

    public LogicalLevel(GameCharacter player, List<GameObject> obstacles, List<GameCharacter> enemies,
            List<AIController> enemyAI, OccupancyManager occupancy,
            DecisionStrategy decisionStrategy, CommandsFactory commandsFactory) {
        super(player, obstacles, enemies, occupancy);
        this.occupancyManager = occupancy;
        this.enemyAI = enemyAI;
        this.decisionStrategy = decisionStrategy;
        this.commandsFactory = commandsFactory;
    }

    @Override
    public OccupancyManager getOccupancy() {
        return occupancyManager;
    }

    public void collectEnemyCommands(List<Command> buffer) {
        for (int i = 0; i < enemies.size(); i++) {
            GameCharacter enemy = enemies.get(i);
            if (enemy.getHealth() <= 0) {
                continue;
            }
            if (enemy.movementProgress < 1f) {
                continue;
            }
            Decision decision = decisionStrategy.recommend(enemy, player);
            boolean wantsShoot = decision.shouldShoot() || enemyAI.get(i).shouldShoot();
            if (wantsShoot) {
                if (isShotReady(enemy)) {
                    Direction aimDirection = directionTowards(enemy.coordinates, player.coordinates);
                    if (aimDirection != null) {
                        enemy.setFacingDirection(aimDirection);
                    }
                    buffer.add(commandsFactory.createShootCommand(enemy));
                    triggerShotCooldown(enemy);
                }
                continue;
            }
            Direction dir = decision.getDirection();
            if (dir == null || dir == Direction.IDLE) {
                dir = enemyAI.get(i).chooseDirection();
            }
            buffer.add(commandsFactory.createMoveCommand(enemy, dir, occupancyManager));
        }
    }

    @Override
    protected void removeEnemyAt(int index) {
        super.removeEnemyAt(index);
        if (index >= 0 && index < enemyAI.size()) {
            enemyAI.remove(index);
        }
    }
}
