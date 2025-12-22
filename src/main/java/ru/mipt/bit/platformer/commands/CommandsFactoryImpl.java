package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.graphics.HealthBarDisplay;
import ru.mipt.bit.platformer.game.commands.Command;
import ru.mipt.bit.platformer.game.commands.CommandsFactory;
import ru.mipt.bit.platformer.game.entities.Direction;
import ru.mipt.bit.platformer.game.entities.GameCharacter;
import ru.mipt.bit.platformer.game.entities.MovableEntity;
import ru.mipt.bit.platformer.game.commands.OccupancyManager;

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
