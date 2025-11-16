package ru.mipt.bit.platformer.commands;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.classes.Direction;
import ru.mipt.bit.platformer.classes.MovableEntity;
import ru.mipt.bit.platformer.logic.OccupancyManager;

public class MoveCommand implements Command {
    private final MovableEntity entity;
    private final Direction direction;
    private final OccupancyManager occupancy;

    public MoveCommand(MovableEntity entity, Direction direction, OccupancyManager occupancy) {
        this.entity = entity;
        this.direction = direction;
        this.occupancy = occupancy;
    }

    @Override
    public void execute() {
        if (direction == Direction.IDLE) {
            return;
        }
        if (entity.movementProgress < 1f) {
            return;
        }
        entity.setFacingDirection(direction);
        GridPoint2 from = entity.coordinates;
        if (!occupancy.canMove(entity, from, direction)) {
            return;
        }
        GridPoint2 to = new GridPoint2(from).add(direction.vector);
        entity.destinationCoordinates.set(to);
        entity.movementProgress = 0f;
        occupancy.reserve(entity, from, to);
    }
}
