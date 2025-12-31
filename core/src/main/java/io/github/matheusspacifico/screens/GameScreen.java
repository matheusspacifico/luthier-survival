package io.github.matheusspacifico.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.matheusspacifico.Main;
import io.github.matheusspacifico.utils.Constants;
import io.github.matheusspacifico.world.GameWorld;

public class GameScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private GameWorld world;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        world = new GameWorld();

        Gdx.input.setCursorCatched(false);
        Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.None);
    }

    @Override
    public void render(float delta) {
        // Update world
        world.update(delta);

        // Clear screen
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Render world
        batch.begin();
        world.render(batch);
        world.renderUI(batch);
        batch.end();

        // Render crosshair on top
        renderCrosshair();
    }

    private void renderCrosshair() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
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
        world.dispose();
    }
}
