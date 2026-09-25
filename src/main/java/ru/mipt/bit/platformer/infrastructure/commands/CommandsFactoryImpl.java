package ru.mipt.bit.platformer.infrastructure.commands;

import ru.mipt.bit.platformer.application.commands.Command;
import ru.mipt.bit.platformer.application.commands.CommandsFactory;
import ru.mipt.bit.platformer.application.level.OccupancyManager;
import ru.mipt.bit.platformer.domain.entities.Direction;
import ru.mipt.bit.platformer.domain.entities.GameCharacter;
import ru.mipt.bit.platformer.domain.entities.MovableEntity;
import ru.mipt.bit.platformer.infrastructure.graphics.HealthBarDisplay;

public class CommandsFactoryImpl implements CommandsFactory {
    private final HealthBarDisplay healthBarDisplay;

    public CommandsFactoryImpl(HealthBarDisplay healthBarDisplay) {
        this.healthBarDisplay = healthBarDisplay;
    }

    @Override
    public Command createMoveCommand(MovableEntity entity, Direction direction, OccupancyManager occupancy) {
        return new MoveCommand(entity, direction, occupancy);
    }

    @Override
    public Command createShootCommand(GameCharacter shooter) {
        return new ShootCommand(shooter);
    }

    @Override
    public Command createToggleHealthBarCommand() {
        return new ToggleHealthBarCommand(healthBarDisplay);
    }
}
