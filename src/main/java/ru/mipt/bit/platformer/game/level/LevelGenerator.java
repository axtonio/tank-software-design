package ru.mipt.bit.platformer.game.level;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import ru.mipt.bit.platformer.game.commands.CommandsFactory;
import ru.mipt.bit.platformer.game.commands.OccupancyManager;

public interface LevelGenerator {
    LogicalLevel generate(TiledMapTileLayer groundLayer, OccupancyManager occupancy,
            CommandsFactory commandsFactory);
}
