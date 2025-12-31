package io.github.matheusspacifico.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
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

public class SettingsScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private GlyphLayout layout;

    private Rectangle windowedButton;
    private Rectangle borderlessButton;
    private Rectangle fullscreenButton;
    private Rectangle backButton;

    private int hoveredButton = -1;
    private String currentMode;

    private static final float BUTTON_WIDTH = 300;
    private static final float BUTTON_HEIGHT = 50;
    private static final float BUTTON_SPACING = 15;

    public SettingsScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        titleFont = new BitmapFont();
        titleFont.getData().setScale(3f);

        buttonFont = new BitmapFont();
        buttonFont.getData().setScale(1.8f);

        float centerX = Constants.SCREEN_WIDTH / 2f - BUTTON_WIDTH / 2f;
        float startY = Constants.SCREEN_HEIGHT / 2f + BUTTON_HEIGHT;

        windowedButton = new Rectangle(centerX, startY, BUTTON_WIDTH, BUTTON_HEIGHT);
        borderlessButton = new Rectangle(centerX, startY - BUTTON_HEIGHT - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        fullscreenButton = new Rectangle(centerX, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton = new Rectangle(centerX, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 3.5f, BUTTON_WIDTH, BUTTON_HEIGHT);

        if (Gdx.graphics.isFullscreen()) {
            currentMode = "fullscreen";
        } else {
            DisplayMode dm = Gdx.graphics.getDisplayMode();
            if (Gdx.graphics.getWidth() == dm.width && Gdx.graphics.getHeight() == dm.height) {
                currentMode = "borderless";
            } else {
                currentMode = "windowed";
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        Vector2 mouse = new Vector2(Gdx.input.getX(), Constants.SCREEN_HEIGHT - Gdx.input.getY());
        hoveredButton = -1;
        if (windowedButton.contains(mouse)) hoveredButton = 0;
        else if (borderlessButton.contains(mouse)) hoveredButton = 1;
        else if (fullscreenButton.contains(mouse)) hoveredButton = 2;
        else if (backButton.contains(mouse)) hoveredButton = 3;

        if (Gdx.input.justTouched()) {
            if (hoveredButton == 0) {
                setWindowed();
            } else if (hoveredButton == 1) {
                setBorderless();
            } else if (hoveredButton == 2) {
                setFullscreen();
            } else if (hoveredButton == 3) {
                game.setScreen(new MenuScreen(game));
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawButton(windowedButton, 0, currentMode.equals("windowed"));
        drawButton(borderlessButton, 1, currentMode.equals("borderless"));
        drawButton(fullscreenButton, 2, currentMode.equals("fullscreen"));
        drawButton(backButton, 3, false);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.6f, 0.6f, 0.7f, 1f);
        drawButtonBorder(windowedButton);
        drawButtonBorder(borderlessButton);
        drawButtonBorder(fullscreenButton);
        drawButtonBorder(backButton);
        shapeRenderer.end();

        batch.begin();

        titleFont.setColor(Color.WHITE);
        drawCenteredText(titleFont, "SETTINGS", Constants.SCREEN_HEIGHT - 150);

        titleFont.getData().setScale(1.5f);
        titleFont.setColor(Color.LIGHT_GRAY);
        drawCenteredText(titleFont, "Display Mode", Constants.SCREEN_HEIGHT - 220);
        titleFont.getData().setScale(3f);

        buttonFont.setColor(Color.WHITE);
        drawCenteredText(buttonFont, "Windowed", windowedButton.y + BUTTON_HEIGHT / 2 + 8);
        drawCenteredText(buttonFont, "Borderless", borderlessButton.y + BUTTON_HEIGHT / 2 + 8);
        drawCenteredText(buttonFont, "Fullscreen", fullscreenButton.y + BUTTON_HEIGHT / 2 + 8);
        drawCenteredText(buttonFont, "Back", backButton.y + BUTTON_HEIGHT / 2 + 8);

        batch.end();
    }

    private void setWindowed() {
        Gdx.graphics.setWindowedMode(1280, 720);
        currentMode = "windowed";
    }

    private void setBorderless() {
        DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setWindowedMode(displayMode.width, displayMode.height);
        currentMode = "borderless";
    }

    private void setFullscreen() {
        DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);
        currentMode = "fullscreen";
    }

    private void drawButton(Rectangle rect, int index, boolean selected) {
        if (selected) {
            shapeRenderer.setColor(0.2f, 0.5f, 0.3f, 1f);
        } else if (hoveredButton == index) {
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
