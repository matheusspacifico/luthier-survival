package io.github.matheusspacifico.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.matheusspacifico.Main;
import io.github.matheusspacifico.ui.PauseMenu;
import io.github.matheusspacifico.utils.Constants;
import io.github.matheusspacifico.world.GameWorld;

public class GameScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private GameWorld world;

    private OrthographicCamera camera;
    private Viewport viewport;

    private PauseMenu pauseMenu;
    private boolean isPaused = false;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, camera);
        viewport.apply(true);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        world = new GameWorld();
        pauseMenu = new PauseMenu(camera, viewport);

        Gdx.input.setCursorCatched(false);
        Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.None);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (isPaused) {
                if (pauseMenu.getCurrentState() == PauseMenu.State.MAIN) {
                    resumeGame();
                }
            } else {
                pauseGame();
            }
        }

        if (isPaused) {
            PauseMenu.Action action = pauseMenu.update();
            handlePauseMenuAction(action);
        } else {
            world.update(delta);
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();
        world.render(batch);
        world.renderUI(batch);
        batch.end();

        if (!isPaused) {
            renderCrosshair();
        }

        if (isPaused) {
            pauseMenu.render();
        }
    }

    private void handlePauseMenuAction(PauseMenu.Action action) {
        switch (action) {
            case RESUME:
                resumeGame();
                break;
            case SETTINGS:
                game.setScreen(new SettingsScreen(game, this));
                break;
            case EXIT_TO_MENU:
                game.setScreen(new MenuScreen(game));
                break;
            case NONE:
            default:
                break;
        }
    }

    private void pauseGame() {
        isPaused = true;
        pauseMenu.reset();
        Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.Arrow);
    }

    private void resumeGame() {
        isPaused = false;
        Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.None);
    }

    public void onReturnFromSettings() {
        Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.Arrow);
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
        viewport.update(width, height, true);
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
        pauseMenu.dispose();
    }
}
