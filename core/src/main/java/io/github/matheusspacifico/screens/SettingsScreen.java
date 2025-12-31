package io.github.matheusspacifico.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.matheusspacifico.Main;
import io.github.matheusspacifico.utils.Constants;

public class SettingsScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont titleFont;
    private BitmapFont subtitleFont;
    private BitmapFont buttonFont;
    private GlyphLayout layout;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Rectangle windowedButton;
    private Rectangle borderlessButton;
    private Rectangle backButton;

    private int hoveredButton = -1;
    private String currentMode;

    private GameScreen returnToGameScreen;

    private static final float BUTTON_WIDTH = 300;
    private static final float BUTTON_HEIGHT = 50;
    private static final float BUTTON_SPACING = 15;

    public SettingsScreen(Main game) {
        this(game, null);
    }

    public SettingsScreen(Main game, GameScreen returnToGameScreen) {
        this.game = game;
        this.returnToGameScreen = returnToGameScreen;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, camera);
        viewport.apply(true);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans.ttf"));

        titleFont = createFont(56);
        subtitleFont = createFont(24);
        buttonFont = createFont(32);

        float centerX = Constants.SCREEN_WIDTH / 2f - BUTTON_WIDTH / 2f;
        float startY = Constants.SCREEN_HEIGHT / 2f + BUTTON_HEIGHT;

        windowedButton = new Rectangle(centerX, startY, BUTTON_WIDTH, BUTTON_HEIGHT);
        borderlessButton = new Rectangle(centerX, startY - BUTTON_HEIGHT - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton = new Rectangle(centerX, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 2.5f, BUTTON_WIDTH, BUTTON_HEIGHT);

        detectCurrentMode();
    }

    private BitmapFont createFont(int size) {
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
        parameter.magFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
        return fontGenerator.generateFont(parameter);
    }

    private void detectCurrentMode() {
        if (Gdx.graphics.isFullscreen()) {
            currentMode = "borderless";
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

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Convert mouse coordinates to world coordinates
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        hoveredButton = -1;
        if (windowedButton.contains(mousePos.x, mousePos.y)) hoveredButton = 0;
        else if (borderlessButton.contains(mousePos.x, mousePos.y)) hoveredButton = 1;
        else if (backButton.contains(mousePos.x, mousePos.y)) hoveredButton = 2;

        if (Gdx.input.justTouched()) {
            if (hoveredButton == 0) {
                setWindowed();
            } else if (hoveredButton == 1) {
                setBorderless();
            } else if (hoveredButton == 2) {
                goBack();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            goBack();
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawButton(windowedButton, 0, currentMode.equals("windowed"));
        drawButton(borderlessButton, 1, currentMode.equals("borderless"));
        drawButton(backButton, 2, false);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.6f, 0.6f, 0.7f, 1f);
        drawButtonBorder(windowedButton);
        drawButtonBorder(borderlessButton);
        drawButtonBorder(backButton);
        shapeRenderer.end();

        batch.begin();

        titleFont.setColor(Color.WHITE);
        drawCenteredText(titleFont, "SETTINGS", Constants.SCREEN_HEIGHT - 150);

        subtitleFont.setColor(Color.LIGHT_GRAY);
        drawCenteredText(subtitleFont, "Display Mode", Constants.SCREEN_HEIGHT - 220);

        if (returnToGameScreen != null) {
            subtitleFont.setColor(Color.GRAY);
            drawCenteredText(subtitleFont, "(Game Paused)", Constants.SCREEN_HEIGHT - 250);
        }

        buttonFont.setColor(Color.WHITE);
        drawCenteredText(buttonFont, "Windowed", windowedButton.y + BUTTON_HEIGHT / 2 + 10);
        drawCenteredText(buttonFont, "Borderless", borderlessButton.y + BUTTON_HEIGHT / 2 + 10);
        drawCenteredText(buttonFont, "Back", backButton.y + BUTTON_HEIGHT / 2 + 10);

        batch.end();
    }

    private void goBack() {
        if (returnToGameScreen != null) {
            returnToGameScreen.onReturnFromSettings();
            game.setScreen(returnToGameScreen);
        } else {
            game.setScreen(new MenuScreen(game));
        }
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
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

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
        fontGenerator.dispose();
        titleFont.dispose();
        subtitleFont.dispose();
        buttonFont.dispose();
    }
}
