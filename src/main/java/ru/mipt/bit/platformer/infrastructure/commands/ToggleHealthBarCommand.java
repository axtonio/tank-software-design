package ru.mipt.bit.platformer.infrastructure.commands;

import ru.mipt.bit.platformer.application.commands.Command;
import ru.mipt.bit.platformer.infrastructure.graphics.HealthBarDisplay;

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
