package ru.mipt.bit.platformer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.graphics.WorldRenderer;
import ru.mipt.bit.platformer.game.pipeline.GameLoop;

public class GameDesktopLauncher implements ApplicationListener {
    private ApplicationContext applicationContext;
    private WorldRenderer worldRenderer;
    private GameLoop gameLoop;

    @Override
    public void create() {
        applicationContext = new AnnotationConfigApplicationContext(GameConfig.class);
        worldRenderer = applicationContext.getBean(WorldRenderer.class);
        gameLoop = applicationContext.getBean(GameLoop.class);
    }

    @Override
    public void render() {
        gameLoop.update(Gdx.graphics.getDeltaTime());
        worldRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        if (applicationContext != null && applicationContext instanceof AnnotationConfigApplicationContext) {
            ((AnnotationConfigApplicationContext) applicationContext).close();
        }
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
