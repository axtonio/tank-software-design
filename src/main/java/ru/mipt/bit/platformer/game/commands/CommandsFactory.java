package ru.mipt.bit.platformer.game.commands;

import ru.mipt.bit.platformer.game.entities.MovableEntity;
import ru.mipt.bit.platformer.game.entities.Direction;
import ru.mipt.bit.platformer.game.entities.GameCharacter;

public interface CommandsFactory {
    Command createMoveCommand(MovableEntity entity, Direction direction, OccupancyManager occupancy);

    Command createShootCommand(GameCharacter shooter);

    Command createToggleHealthBarCommand();
}
