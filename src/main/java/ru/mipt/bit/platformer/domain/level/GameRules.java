package ru.mipt.bit.platformer.domain.level;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.domain.entities.Direction;
import ru.mipt.bit.platformer.domain.entities.GameCharacter;
import ru.mipt.bit.platformer.domain.entities.GameObject;
import ru.mipt.bit.platformer.domain.entities.Positionable;
import ru.mipt.bit.platformer.domain.entities.Projectile;
import ru.mipt.bit.platformer.domain.entities.ProjectileRegistry;

public class GameRules implements LevelObservable, ProjectileRegistry {
    protected static final float ENEMY_SHOT_COOLDOWN = 0.7f;
    protected final GameCharacter player;
    protected final List<GameObject> obstacles;
    protected final List<GameCharacter> enemies;
    protected final ArrayList<Projectile> bullets = new ArrayList<>();
    protected final ArrayList<LevelObserver> observers = new ArrayList<>();
    protected final LevelMap occupancy;
    protected boolean playerRemoved = false;
    protected final Map<GameCharacter, Float> enemyShotCooldowns = new HashMap<>();

    public GameRules(GameCharacter player, List<GameObject> obstacles, List<GameCharacter> enemies,
            LevelMap occupancy) {
        this.player = player;
        this.obstacles = obstacles;
        this.enemies = enemies;
        this.occupancy = occupancy;

        this.occupancy.registerPosition(player, player.coordinates);
        for (GameCharacter enemy : enemies) {
            this.occupancy.registerPosition(enemy, enemy.coordinates);
        }
    }

    public GameCharacter getPlayer() {
        return player;
    }

    public List<GameObject> getObstacles() {
        return Collections.unmodifiableList(obstacles);
    }

    public List<GameCharacter> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    public LevelMap getOccupancy() {
        return occupancy;
    }

    public void tick(float deltaTime) {
        updateMovement(player, deltaTime);
        int index = 0;
        while (index < enemies.size()) {
            GameCharacter enemy = enemies.get(index);
            if (enemy.getHealth() <= 0) {
                removeEnemyAt(index);
                continue;
            }
            updateMovement(enemy, deltaTime);
            index++;
        }
        updateBullets(deltaTime);
        updateEnemyCooldowns(deltaTime);
    }

    private void updateMovement(GameCharacter tank, float deltaTime) {
        if (tank.getHealth() <= 0) {
            return;
        }
        tank.movementProgress = continueProgress(tank.movementProgress, deltaTime, tank.movementSpeed);
        if (isEqual(tank.movementProgress, 1f)) {
            tank.coordinates.set(tank.destinationCoordinates);
            occupancy.commitPosition(tank, tank.coordinates);
            occupancy.release(tank);
        } else {
            occupancy.updateReservation(tank, tank.coordinates, tank.destinationCoordinates);
        }
    }

    protected boolean isShotReady(GameCharacter enemy) {
        Float cooldown = enemyShotCooldowns.get(enemy);
        return cooldown == null || cooldown <= 0f;
    }

    protected void triggerShotCooldown(GameCharacter enemy) {
        enemyShotCooldowns.put(enemy, ENEMY_SHOT_COOLDOWN);
    }

    private void updateEnemyCooldowns(float deltaTime) {
        Iterator<Map.Entry<GameCharacter, Float>> iterator = enemyShotCooldowns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<GameCharacter, Float> entry = iterator.next();
            float updated = entry.getValue() - deltaTime;
            if (updated <= 0f || entry.getKey().getHealth() <= 0) {
                iterator.remove();
            } else {
                entry.setValue(updated);
            }
        }
    }

    private void updateBullets(float deltaTime) {
        int index = 0;
        while (index < bullets.size()) {
            Projectile bullet = bullets.get(index);
            bullet.movementProgress = continueProgress(bullet.movementProgress, deltaTime, bullet.movementSpeed);
            if (isEqual(bullet.movementProgress, 1f)) {
                bullet.coordinates.set(bullet.destinationCoordinates);
                if (handleBulletArrival(bullet)) {
                    continue;
                }
                if (!scheduleBulletAdvance(bullet)) {
                    removeBullet(bullet);
                    continue;
                }
            }
            index++;
        }
    }

    @Override
    public boolean register(Projectile bullet) {
        if (bullet == null) {
            return false;
        }
        if (!occupancy.isWithinBounds(bullet.coordinates)) {
            return false;
        }
        if (occupancy.isStaticObstacle(bullet.coordinates)) {
            return false;
        }
        if (damageTankAt(bullet.coordinates, bullet.getOwner(), bullet.getDamage())) {
            return true;
        }
        bullets.add(bullet);
        notifyAdded(bullet);
        bullet.destinationCoordinates.set(bullet.coordinates);
        bullet.movementProgress = 1f;
        Projectile collision = findBulletCollision(bullet);
        if (collision != null) {
            removeBullet(collision);
            removeBullet(bullet);
            return true;
        }
        if (!scheduleBulletAdvance(bullet)) {
            removeBullet(bullet);
        }
        return true;
    }

    private boolean scheduleBulletAdvance(Projectile bullet) {
        GridPoint2 next = bullet.peekNextStep();
        if (!occupancy.isWithinBounds(next)) {
            return false;
        }
        if (occupancy.isStaticObstacle(next)) {
            removeObstacleAt(next);
            return false;
        }
        bullet.destinationCoordinates.set(next);
        bullet.movementProgress = 0f;
        return true;
    }

    private boolean handleBulletArrival(Projectile bullet) {
        if (!occupancy.isWithinBounds(bullet.coordinates)) {
            removeBullet(bullet);
            return true;
        }
        if (occupancy.isStaticObstacle(bullet.coordinates)) {
            removeObstacleAt(bullet.coordinates);
            removeBullet(bullet);
            return true;
        }
        if (damageTankAt(bullet.coordinates, bullet.getOwner(), bullet.getDamage())) {
            removeBullet(bullet);
            return true;
        }
        Projectile collision = findBulletCollision(bullet);
        if (collision != null) {
            removeBullet(collision);
            removeBullet(bullet);
            return true;
        }
        return false;
    }

    private Projectile findBulletCollision(Projectile bullet) {
        for (Projectile other : bullets) {
            if (other == bullet) {
                continue;
            }
            if (other.getOwner() == bullet.getOwner()) {
                continue;
            }
            if (other.coordinates.equals(bullet.coordinates)) {
                return other;
            }
        }
        return null;
    }

    private boolean damageTankAt(GridPoint2 position, GameCharacter shooter, int damage) {
        if (shooter == null || damage <= 0) {
            return false;
        }
        if (shooter == player) {
            for (int i = 0; i < enemies.size(); i++) {
                GameCharacter enemy = enemies.get(i);
                if (enemy.coordinates.equals(position)) {
                    enemy.takeDamage(damage);
                    if (enemy.getHealth() <= 0) {
                        removeEnemyAt(i);
                    }
                    return true;
                }
            }
            return false;
        }
        if (player != null && player != shooter && player.coordinates.equals(position)) {
            player.takeDamage(damage);
            if (player.getHealth() <= 0) {
                removePlayer();
            }
            return true;
        }
        return false;
    }

    private boolean removeObstacleAt(GridPoint2 position) {
        for (int i = 0; i < obstacles.size(); i++) {
            GameObject obstacle = obstacles.get(i);
            if (obstacle.coordinates.equals(position)) {
                obstacles.remove(i);
                occupancy.removeStaticObstacle(position);
                notifyRemoved(obstacle);
                return true;
            }
        }
        return false;
    }

    private void removePlayer() {
        if (playerRemoved) {
            return;
        }
        playerRemoved = true;
        occupancy.release(player);
        occupancy.unregisterPosition(player);
        notifyRemoved(player);
    }

    protected void removeEnemyAt(int index) {
        if (index < 0 || index >= enemies.size()) {
            return;
        }
        GameCharacter removed = enemies.remove(index);
        occupancy.release(removed);
        occupancy.unregisterPosition(removed);
        notifyRemoved(removed);
    }

    private void removeBullet(Projectile bullet) {
        if (bullets.remove(bullet)) {
            notifyRemoved(bullet);
        }
    }

    @Override
    public void addObserver(LevelObserver observer) {
        if (observer == null) {
            return;
        }
        observers.add(observer);
        observer.onObjectAdded(player);
        for (GameObject obstacle : obstacles) {
            observer.onObjectAdded(obstacle);
        }
        for (GameCharacter enemy : enemies) {
            observer.onObjectAdded(enemy);
        }
        for (Projectile bullet : bullets) {
            observer.onObjectAdded(bullet);
        }
    }

    @Override
    public void removeObserver(LevelObserver observer) {
        observers.remove(observer);
    }

    protected void notifyAdded(Positionable object) {
        for (LevelObserver observer : observers) {
            observer.onObjectAdded(object);
        }
    }

    protected void notifyRemoved(Positionable object) {
        for (LevelObserver observer : observers) {
            observer.onObjectRemoved(object);
        }
    }

    protected Direction directionTowards(GridPoint2 from, GridPoint2 to) {
        if (from == null || to == null || from.equals(to)) {
            return null;
        }
        int dx = to.x - from.x;
        int dy = to.y - from.y;
        if (Math.abs(dx) >= Math.abs(dy)) {
            if (dx > 0) {
                return Direction.RIGHT;
            } else if (dx < 0) {
                return Direction.LEFT;
            }
        }
        if (dy > 0) {
            return Direction.UP;
        } else if (dy < 0) {
            return Direction.DOWN;
        }
        return null;
    }
}
