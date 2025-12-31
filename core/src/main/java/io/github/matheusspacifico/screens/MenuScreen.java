package io.github.matheusspacifico.screens;

import com.badlogic.gdx.Gdx;
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

public class MenuScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont titleFont;
    private BitmapFont taglineFont;
    private BitmapFont buttonFont;
    private GlyphLayout layout;

    private OrthographicCamera camera;
    private Viewport viewport;

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
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, camera);
        viewport.apply(true);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        // Load TTF font and generate at exact sizes needed
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans.ttf"));

        titleFont = createFont(72);
        taglineFont = createFont(28);
        buttonFont = createFont(36);

        float centerX = Constants.SCREEN_WIDTH / 2f - BUTTON_WIDTH / 2f;
        float startY = Constants.SCREEN_HEIGHT / 2f + BUTTON_HEIGHT;

        startButton = new Rectangle(centerX, startY, BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton = new Rectangle(centerX, startY - BUTTON_HEIGHT - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        quitButton = new Rectangle(centerX, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 2, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    private BitmapFont createFont(int size) {
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
        parameter.magFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
        return fontGenerator.generateFont(parameter);
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
        if (startButton.contains(mousePos.x, mousePos.y)) hoveredButton = 0;
        else if (settingsButton.contains(mousePos.x, mousePos.y)) hoveredButton = 1;
        else if (quitButton.contains(mousePos.x, mousePos.y)) hoveredButton = 2;

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

        titleFont.setColor(Color.WHITE);
        drawCenteredText(titleFont, "LUTHIER SURVIVAL", Constants.SCREEN_HEIGHT - 150);

        taglineFont.setColor(Color.LIGHT_GRAY);
        drawCenteredText(taglineFont, "Craft by day. Shred by night.", Constants.SCREEN_HEIGHT - 220);

        buttonFont.setColor(Color.WHITE);
        drawCenteredText(buttonFont, "START GAME", startButton.y + BUTTON_HEIGHT / 2 + 12);
        drawCenteredText(buttonFont, "SETTINGS", settingsButton.y + BUTTON_HEIGHT / 2 + 12);
        drawCenteredText(buttonFont, "QUIT", quitButton.y + BUTTON_HEIGHT / 2 + 12);

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
        taglineFont.dispose();
        buttonFont.dispose();
    }
}
