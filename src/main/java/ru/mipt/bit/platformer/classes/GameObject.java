package ru.mipt.bit.platformer.classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.GridPoint2;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

public abstract class GameObject {
    public Texture texture;
    public TextureRegion graphics;
    public Rectangle boundingBox;
    public GridPoint2 coordinates;
    
    public GameObject(String imagePath, int xCoordinate, int yCoordinate) {
        this.texture = new Texture(imagePath);
        this.graphics = new TextureRegion(this.texture);
        this.boundingBox = createBoundingRectangle(this.graphics);
        this.coordinates = new GridPoint2(xCoordinate, yCoordinate);
    }
    
    public void dispose() {
        texture.dispose();
    }
}