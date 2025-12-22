package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.game.commands.Command;
import ru.mipt.bit.platformer.game.entities.GameCharacter;

public class ShootCommand implements Command {
    private final GameCharacter tank;

    public ShootCommand(GameCharacter tank) {
        this.tank = tank;
    }

    @Override
    public void execute() {
        if (tank == null) {
            return;
        }
        tank.fire();
    }
}
