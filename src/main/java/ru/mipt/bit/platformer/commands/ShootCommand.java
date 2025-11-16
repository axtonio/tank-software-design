package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.classes.Tank;

public class ShootCommand implements Command {
    private final Tank tank;

    public ShootCommand(Tank tank) {
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

