package io.github.matheusspacifico.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.matheusspacifico.entities.Player;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Player player;

    @Override
    public void show() {
        batch = new SpriteBatch();
        player = new Player();
    }

    @Override
    public void render(float delta) {
        player.update(delta);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();
        player.render(batch);
        batch.end();
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
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
    }
}
