package ru.mipt.bit.platformer.application.level;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import ru.mipt.bit.platformer.application.commands.CommandsFactory;

public interface LevelGenerator {
    LogicalLevel generate(TiledMapTileLayer groundLayer, OccupancyManager occupancy,
            CommandsFactory commandsFactory);
}
