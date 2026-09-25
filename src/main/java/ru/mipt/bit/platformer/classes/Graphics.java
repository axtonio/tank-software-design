package ru.mipt.bit.platformer.classes;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Graphics {
    public Texture texture;

    public TextureRegion graphics;
    public Rectangle rectangle;

    public TextureRegion textureRegion;
    public Rectangle boundingBox;

    public Graphics(String imagePath) {
        this.texture = new Texture(imagePath);
        this.graphics = new TextureRegion(this.texture);
        this.rectangle = createBoundingRectangle(this.graphics);

        // For backward compatibility with tests
        this.textureRegion = this.graphics;
        this.boundingBox = this.rectangle;
    }

    public void dispose() {
        texture.dispose();
    }
}