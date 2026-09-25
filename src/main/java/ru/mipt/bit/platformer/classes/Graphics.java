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

    private final boolean ownsTexture;

    public Graphics(String imagePath) {
        this(new Texture(imagePath), true);
    }

    public Graphics(Graphics prototype) {
        this(prototype.texture, false);
    }

    private Graphics(Texture texture, boolean ownsTexture) {
        this.texture = texture;
        this.graphics = new TextureRegion(this.texture);
        this.rectangle = createBoundingRectangle(this.graphics);
        this.textureRegion = this.graphics;
        this.boundingBox = this.rectangle;
        this.ownsTexture = ownsTexture;
    }

    public void dispose() {
        if (ownsTexture && texture != null) {
            texture.dispose();
        }
    }
}