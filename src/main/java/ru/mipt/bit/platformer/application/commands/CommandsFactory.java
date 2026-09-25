package ru.mipt.bit.platformer.application.commands;

import ru.mipt.bit.platformer.application.level.OccupancyManager;
import ru.mipt.bit.platformer.domain.entities.Direction;
import ru.mipt.bit.platformer.domain.entities.GameCharacter;
import ru.mipt.bit.platformer.domain.entities.MovableEntity;;

public interface CommandsFactory {
    Command createMoveCommand(MovableEntity entity, Direction direction, OccupancyManager occupancy);

    Command createShootCommand(GameCharacter shooter);

    Command createToggleHealthBarCommand();
}
