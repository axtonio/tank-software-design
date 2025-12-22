package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.graphics.HealthBarDisplay;
import ru.mipt.bit.platformer.game.commands.Command;

public class ToggleHealthBarCommand implements Command {
    private final HealthBarDisplay display;

    public ToggleHealthBarCommand(HealthBarDisplay display) {
        this.display = display;
    }

    @Override
    public void execute() {
        display.toggle();
    }
}
