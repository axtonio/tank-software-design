package ru.mipt.bit.platformer.game.pipeline;
import ru.mipt.bit.platformer.game.level.LogicalLevel;
import ru.mipt.bit.platformer.game.input.InputController;
import ru.mipt.bit.platformer.game.commands.CommandsFactory;
import ru.mipt.bit.platformer.game.commands.Command;
import ru.mipt.bit.platformer.game.entities.GameCharacter;
import ru.mipt.bit.platformer.game.entities.Direction;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class GameLoop {
    private final LogicalLevel logicalLevel;
    private final InputController inputController;
    private final CommandsFactory commandsFactory;
    private final List<Command> pendingCommands = new ArrayList<>();

    public GameLoop(LogicalLevel logicalLevel, InputController inputController,
            CommandsFactory commandsFactory) {
        this.logicalLevel = logicalLevel;
        this.inputController = inputController;
        this.commandsFactory = commandsFactory;
    }

    public void update(float deltaTime) {
        handleInput();
        executeCommands();
        logicalLevel.tick(deltaTime);
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.L)) {
            commandsFactory.createToggleHealthBarCommand().execute();
        }

        pendingCommands.clear();
        GameCharacter player = logicalLevel.getPlayer();
        if (player == null || player.getHealth() <= 0) {
            logicalLevel.collectEnemyCommands(pendingCommands);
            return;
        }

        Direction direction = inputController.getDirection();
        pendingCommands.add(commandsFactory.createMoveCommand(player, direction, logicalLevel.getOccupancy()));
        if (inputController.isFireButtonPressed()) {
            pendingCommands.add(commandsFactory.createShootCommand(player));
        }
        logicalLevel.collectEnemyCommands(pendingCommands);
    }

    private void executeCommands() {
        for (Command command : pendingCommands) {
            if (command != null) {
                command.execute();
            }
        }
        pendingCommands.clear();
    }
}
