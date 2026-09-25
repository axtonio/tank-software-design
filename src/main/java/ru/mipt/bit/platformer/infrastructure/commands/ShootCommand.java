package ru.mipt.bit.platformer.infrastructure.commands;

import ru.mipt.bit.platformer.application.commands.Command;
import ru.mipt.bit.platformer.domain.entities.GameCharacter;

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
