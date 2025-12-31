package io.github.matheusspacifico.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.matheusspacifico.Main;
import io.github.matheusspacifico.utils.Constants;

public class MenuScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private GlyphLayout layout;

    private Rectangle startButton;
    private Rectangle settingsButton;
    private Rectangle quitButton;

    private int hoveredButton = -1;

    private static final float BUTTON_WIDTH = 300;
    private static final float BUTTON_HEIGHT = 60;
    private static final float BUTTON_SPACING = 20;

    public MenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        titleFont = new BitmapFont();
        titleFont.getData().setScale(4f);
        titleFont.setColor(Color.WHITE);

        buttonFont = new BitmapFont();
        buttonFont.getData().setScale(2f);
        buttonFont.setColor(Color.WHITE);

        float centerX = Constants.SCREEN_WIDTH / 2f - BUTTON_WIDTH / 2f;
        float startY = Constants.SCREEN_HEIGHT / 2f + BUTTON_HEIGHT;

        startButton = new Rectangle(centerX, startY, BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton = new Rectangle(centerX, startY - BUTTON_HEIGHT - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        quitButton = new Rectangle(centerX, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 2, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        Vector2 mouse = new Vector2(Gdx.input.getX(), Constants.SCREEN_HEIGHT - Gdx.input.getY());
        hoveredButton = -1;
        if (startButton.contains(mouse)) hoveredButton = 0;
        else if (settingsButton.contains(mouse)) hoveredButton = 1;
        else if (quitButton.contains(mouse)) hoveredButton = 2;

        if (Gdx.input.justTouched()) {
            if (hoveredButton == 0) {
                game.setScreen(new GameScreen(game));
            } else if (hoveredButton == 1) {
                game.setScreen(new SettingsScreen(game));
            } else if (hoveredButton == 2) {
                Gdx.app.exit();
            }
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawButton(startButton, 0);
        drawButton(settingsButton, 1);
        drawButton(quitButton, 2);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.6f, 0.6f, 0.7f, 1f);
        drawButtonBorder(startButton);
        drawButtonBorder(settingsButton);
        drawButtonBorder(quitButton);
        shapeRenderer.end();

        batch.begin();

        titleFont.getData().setScale(4f);
        titleFont.setColor(Color.WHITE);
        drawCenteredText(titleFont, "LUTHIER SURVIVAL", Constants.SCREEN_HEIGHT - 150);

        titleFont.getData().setScale(1.5f);
        titleFont.setColor(Color.LIGHT_GRAY);
        drawCenteredText(titleFont, "Craft by day. Shred by night.", Constants.SCREEN_HEIGHT - 220);

        buttonFont.setColor(Color.WHITE);
        drawCenteredText(buttonFont, "START GAME", startButton.y + BUTTON_HEIGHT / 2 + 8);
        drawCenteredText(buttonFont, "SETTINGS", settingsButton.y + BUTTON_HEIGHT / 2 + 8);
        drawCenteredText(buttonFont, "QUIT", quitButton.y + BUTTON_HEIGHT / 2 + 8);

        batch.end();
    }

    private void drawButton(Rectangle rect, int index) {
        if (hoveredButton == index) {
            shapeRenderer.setColor(0.4f, 0.4f, 0.5f, 1f);
        } else {
            shapeRenderer.setColor(0.25f, 0.25f, 0.3f, 1f);
        }
        shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
    }

    private void drawButtonBorder(Rectangle rect) {
        shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
    }

    private void drawCenteredText(BitmapFont font, String text, float y) {
        layout.setText(font, text);
        float x = (Constants.SCREEN_WIDTH - layout.width) / 2f;
        font.draw(batch, text, x, y);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        titleFont.dispose();
        buttonFont.dispose();
    }
}
