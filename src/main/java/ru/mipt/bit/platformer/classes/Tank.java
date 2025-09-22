package ru.mipt.bit.platformer.classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tank extends GameObject {
    public GridPoint2 destinationCoordinates;
    public float movementProgress = 1f;
    public float rotation;
    
    private Direction currentDirection;
    
    public Tank(String imagePath, int xCoordinate, int yCoordinate) {
        super(imagePath, xCoordinate, yCoordinate);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.rotation = 0f;
        this.currentDirection = Direction.RIGHT;
    }
    
    public void tryMove(Direction direction, GameObject obstacle) {
        if (isEqual(movementProgress, 1f)) {
            GridPoint2 targetPosition = getTargetPosition(direction);
            
            if (!obstacle.coordinates.equals(targetPosition)) {
                destinationCoordinates.set(targetPosition);
                movementProgress = 0f;
            }
            
            rotation = direction.getRotation();
            currentDirection = direction;
        }
    }
    
    private GridPoint2 getTargetPosition(Direction direction) {
        GridPoint2 target = new GridPoint2(coordinates);
        switch (direction) {
            case UP:
                target.y++;
                break;
            case LEFT:
                target.x--;
                break;
            case DOWN:
                target.y--;
                break;
            case RIGHT:
                target.x++;
                break;
        }
        return target;
    }
    
    public enum Direction {
        UP(90f),
        LEFT(-180f),
        DOWN(-90f),
        RIGHT(0f);
        
        private final float rotation;
        
        Direction(float rotation) {
            this.rotation = rotation;
        }
        
        public float getRotation() {
            return rotation;
        }
    }
}