package io.github.matheusspacifico.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.matheusspacifico.entities.Player;
import io.github.matheusspacifico.systems.WeaponSystem;
import io.github.matheusspacifico.utils.Constants;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Player player;
    private WeaponSystem weaponSystem;

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        player = new Player();
        weaponSystem = new WeaponSystem();

        Gdx.input.setCursorCatched(false);
        Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.None);
    }

    @Override
    public void render(float delta) {
        player.update(delta);
        weaponSystem.update(delta, player);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();
        player.render(batch);
        weaponSystem.renderProjectiles(batch);
        weaponSystem.renderUI(batch);
        batch.end();

        renderCrosshair();
    }

    private void renderCrosshair() {
        float mouseX = Gdx.input.getX();
        float mouseY = Constants.SCREEN_HEIGHT - Gdx.input.getY();
        float size = Constants.CROSSHAIR_SIZE / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        shapeRenderer.line(mouseX - size, mouseY, mouseX + size, mouseY);
        shapeRenderer.line(mouseX, mouseY - size, mouseX, mouseY + size);

        shapeRenderer.circle(mouseX, mouseY, 3);

        shapeRenderer.end();
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
        shapeRenderer.dispose();
        player.dispose();
        weaponSystem.dispose();
    }
}
