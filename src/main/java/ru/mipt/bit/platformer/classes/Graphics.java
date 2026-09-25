package ru.mipt.bit.platformer.classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

public class Graphics {
    public Texture texture;
    public TextureRegion textureRegion;
    public Rectangle boundingBox;
    
    public Graphics(String imagePath) {
        this.texture = new Texture(imagePath);
        this.textureRegion = new TextureRegion(this.texture);
        this.boundingBox = createBoundingRectangle(this.textureRegion);
    }
    
    public void dispose() {
        texture.dispose();
    }
}