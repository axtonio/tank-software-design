package ru.mipt.bit.platformer.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import ru.mipt.bit.platformer.classes.Tank;
import ru.mipt.bit.platformer.game.entities.Direction;
import ru.mipt.bit.platformer.game.entities.Projectile;
import ru.mipt.bit.platformer.game.entities.ProjectileRegistry;

class ShootCommandTest {

    @Test
    void shootCommandRegistersBullet() {
        Tank tank = new Tank(2, 2, 0.4f);
        tank.setFacingDirection(Direction.UP);
        TestRegistry registry = new TestRegistry();
        tank.setProjectileRegistry(registry);

        new ShootCommand(tank).execute();

        assertNotNull(registry.lastBullet);
        assertEquals(2, registry.lastBullet.coordinates.x);
        assertEquals(3, registry.lastBullet.coordinates.y);
        assertEquals(Direction.UP, registry.lastBullet.getDirection());
    }

    @Test
    void shootCommandWithoutRegistryDoesNothing() {
        Tank tank = new Tank(0, 0, 0.4f);
        new ShootCommand(tank).execute();

        // No registry attached, so nothing should happen and no exception thrown
    }

    private static class TestRegistry implements ProjectileRegistry {
        private Projectile lastBullet;

        @Override
        public boolean register(Projectile bullet) {
            this.lastBullet = bullet;
            return true;
        }
    }
}
