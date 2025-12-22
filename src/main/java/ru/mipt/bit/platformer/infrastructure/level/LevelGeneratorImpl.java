package ru.mipt.bit.platformer.infrastructure.level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.application.commands.CommandsFactory;
import ru.mipt.bit.platformer.application.level.LevelGenerator;
import ru.mipt.bit.platformer.application.level.LogicalLevel;
import ru.mipt.bit.platformer.application.level.OccupancyManager;
import ru.mipt.bit.platformer.domain.ai.AIController;
import ru.mipt.bit.platformer.domain.ai.DecisionStrategy;
import ru.mipt.bit.platformer.domain.entities.GameCharacter;
import ru.mipt.bit.platformer.domain.entities.GameObject;
import ru.mipt.bit.platformer.domain.entities.Positionable;
import ru.mipt.bit.platformer.infrastructure.ai.RandomAIController;
import ru.mipt.bit.platformer.infrastructure.entities.Tank;
import ru.mipt.bit.platformer.infrastructure.entities.Tree;

public class LevelGeneratorImpl implements LevelGenerator {
    private final MapLayout mapLayout;
    private final DecisionStrategy decisionStrategy;

    public LevelGeneratorImpl(MapLayout mapLayout, DecisionStrategy decisionStrategy) {
        this.mapLayout = mapLayout;
        this.decisionStrategy = decisionStrategy;
    }

    @Override
    public LogicalLevel generate(TiledMapTileLayer groundLayer, OccupancyManager occupancy,
            CommandsFactory commandsFactory) {
        GridPoint2 playerCoordinates = mapLayout.layout.get("Player").get(0);
        Tank player = new Tank(playerCoordinates.x, playerCoordinates.y, 0.4f);

        ArrayList<GameObject> obstacles = new ArrayList<>();
        for (GridPoint2 obstacleCoordinates : mapLayout.layout.get("Obstacles")) {
            obstacles.add(new Tree(obstacleCoordinates.x, obstacleCoordinates.y));
        }
        HashSet<GridPoint2> staticObs = new HashSet<>();
        for (Positionable obstacle : obstacles) {
            staticObs.add(new GridPoint2(obstacle.coordinates));
        }
        occupancy.setStaticObstacles(staticObs);

        ArrayList<GameCharacter> enemies = new ArrayList<>();
        ArrayList<AIController> enemyAI = new ArrayList<>();
        Random rnd = new Random();
        int enemyCount = 2;
        HashSet<String> occupied = new HashSet<>();
        occupied.add(player.coordinates.x + "," + player.coordinates.y);
        for (Positionable obstacle : obstacles) {
            occupied.add(obstacle.coordinates.x + "," + obstacle.coordinates.y);
        }
        for (int e = 0; e < enemyCount; e++) {
            int attempts = 0;
            while (attempts < 1000) {
                int x = rnd.nextInt(groundLayer.getWidth());
                int y = rnd.nextInt(groundLayer.getHeight());
                String key = x + "," + y;
                if (!occupied.contains(key)) {
                    Tank enemy = new Tank(x, y, 0.4f);
                    enemies.add(enemy);
                    enemyAI.add(new RandomAIController());
                    occupied.add(key);
                    break;
                }
                attempts++;
            }
        }

        LogicalLevel logicalLevel = new LogicalLevel(player, obstacles, enemies, enemyAI, occupancy,
                decisionStrategy, commandsFactory);
        player.setProjectileRegistry(logicalLevel);
        for (GameCharacter enemy : enemies) {
            enemy.setProjectileRegistry(logicalLevel);
        }
        return logicalLevel;
    }
}
