package io.github.matheusspacifico.ui;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.matheusspacifico.utils.Constants;

public class PauseMenu implements Disposable {

    public enum State {
        MAIN,
        CONFIRM_EXIT
    }

    public enum Action {
        NONE,
        RESUME,
        SETTINGS,
        EXIT_TO_MENU
    }

    private State currentState = State.MAIN;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private BitmapFont warningFont;
    private GlyphLayout layout;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Rectangle resumeButton;
    private Rectangle settingsButton;
    private Rectangle exitButton;

    private Rectangle confirmYesButton;
    private Rectangle confirmNoButton;

    private int hoveredButton = -1;

    private static final float BUTTON_WIDTH = 300;
    private static final float BUTTON_HEIGHT = 50;
    private static final float BUTTON_SPACING = 15;

    private static final float DIALOG_WIDTH = 500;
    private static final float DIALOG_HEIGHT = 250;

    public PauseMenu(OrthographicCamera camera, Viewport viewport) {
        this.camera = camera;
        this.viewport = viewport;

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans.ttf"));
        titleFont = createFont(48);
        buttonFont = createFont(28);
        warningFont = createFont(22);

        initializeButtons();
    }

    private BitmapFont createFont(int size) {
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
        parameter.magFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
        return fontGenerator.generateFont(parameter);
    }

    private void initializeButtons() {
        float centerX = Constants.SCREEN_WIDTH / 2f - BUTTON_WIDTH / 2f;
        float startY = Constants.SCREEN_HEIGHT / 2f + BUTTON_HEIGHT;

        resumeButton = new Rectangle(centerX, startY, BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton = new Rectangle(centerX, startY - BUTTON_HEIGHT - BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton = new Rectangle(centerX, startY - (BUTTON_HEIGHT + BUTTON_SPACING) * 2, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Confirmation dialog buttons
        float dialogX = Constants.SCREEN_WIDTH / 2f - DIALOG_WIDTH / 2f;
        float dialogY = Constants.SCREEN_HEIGHT / 2f - DIALOG_HEIGHT / 2f;
        float confirmButtonWidth = 120;
        float confirmButtonSpacing = 40;

        confirmYesButton = new Rectangle(
            dialogX + DIALOG_WIDTH / 2f - confirmButtonWidth - confirmButtonSpacing / 2f,
            dialogY + 30,
            confirmButtonWidth,
            BUTTON_HEIGHT
        );
        confirmNoButton = new Rectangle(
            dialogX + DIALOG_WIDTH / 2f + confirmButtonSpacing / 2f,
            dialogY + 30,
            confirmButtonWidth,
            BUTTON_HEIGHT
        );
    }

    public Action update() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        if (currentState == State.MAIN) {
            return updateMainMenu(mousePos);
        } else {
            return updateConfirmDialog(mousePos);
        }
    }

    private Action updateMainMenu(Vector3 mousePos) {
        hoveredButton = -1;
        if (resumeButton.contains(mousePos.x, mousePos.y)) hoveredButton = 0;
        else if (settingsButton.contains(mousePos.x, mousePos.y)) hoveredButton = 1;
        else if (exitButton.contains(mousePos.x, mousePos.y)) hoveredButton = 2;

        if (Gdx.input.justTouched()) {
            if (hoveredButton == 0) {
                return Action.RESUME;
            } else if (hoveredButton == 1) {
                return Action.SETTINGS;
            } else if (hoveredButton == 2) {
                currentState = State.CONFIRM_EXIT;
                return Action.NONE;
            }
        }

        return Action.NONE;
    }

    private Action updateConfirmDialog(Vector3 mousePos) {
        hoveredButton = -1;
        if (confirmYesButton.contains(mousePos.x, mousePos.y)) hoveredButton = 10;
        else if (confirmNoButton.contains(mousePos.x, mousePos.y)) hoveredButton = 11;

        if (Gdx.input.justTouched()) {
            if (hoveredButton == 10) {
                return Action.EXIT_TO_MENU;
            } else if (hoveredButton == 11) {
                currentState = State.MAIN;
                return Action.NONE;
            }
        }

        return Action.NONE;
    }

    public void render() {
        // Draw semi-transparent background overlay
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        shapeRenderer.end();

        if (currentState == State.MAIN) {
            renderMainMenu();
        } else {
            renderConfirmDialog();
        }

        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
    }

    private void renderMainMenu() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawButton(resumeButton, 0, false);
        drawButton(settingsButton, 1, false);
        drawButton(exitButton, 2, false);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.6f, 0.6f, 0.7f, 1f);
        drawButtonBorder(resumeButton);
        drawButtonBorder(settingsButton);
        drawButtonBorder(exitButton);
        shapeRenderer.end();

        batch.begin();

        titleFont.setColor(Color.WHITE);
        drawCenteredText(titleFont, "PAUSED", Constants.SCREEN_HEIGHT - 200);

        buttonFont.setColor(Color.WHITE);
        drawCenteredText(buttonFont, "Resume", resumeButton.y + BUTTON_HEIGHT / 2 + 10);
        drawCenteredText(buttonFont, "Settings", settingsButton.y + BUTTON_HEIGHT / 2 + 10);
        drawCenteredText(buttonFont, "Exit to Menu", exitButton.y + BUTTON_HEIGHT / 2 + 10);

        batch.end();
    }

    private void renderConfirmDialog() {
        float dialogX = Constants.SCREEN_WIDTH / 2f - DIALOG_WIDTH / 2f;
        float dialogY = Constants.SCREEN_HEIGHT / 2f - DIALOG_HEIGHT / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.15f, 0.15f, 0.2f, 1f);
        shapeRenderer.rect(dialogX, dialogY, DIALOG_WIDTH, DIALOG_HEIGHT);

        drawButton(confirmYesButton, 10, false);
        drawButton(confirmNoButton, 11, false);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.6f, 0.6f, 0.7f, 1f);
        shapeRenderer.rect(dialogX, dialogY, DIALOG_WIDTH, DIALOG_HEIGHT);
        drawButtonBorder(confirmYesButton);
        drawButtonBorder(confirmNoButton);
        shapeRenderer.end();

        batch.begin();

        titleFont.setColor(Color.WHITE);
        drawCenteredText(titleFont, "Exit to Menu?", dialogY + DIALOG_HEIGHT - 50);

        warningFont.setColor(Color.CORAL);
        drawCenteredText(warningFont, "All progress will be lost!", dialogY + DIALOG_HEIGHT - 110);

        buttonFont.setColor(Color.WHITE);
        layout.setText(buttonFont, "Yes");
        buttonFont.draw(batch, "Yes",
            confirmYesButton.x + (confirmYesButton.width - layout.width) / 2f,
            confirmYesButton.y + BUTTON_HEIGHT / 2 + 10);

        layout.setText(buttonFont, "No");
        buttonFont.draw(batch, "No",
            confirmNoButton.x + (confirmNoButton.width - layout.width) / 2f,
            confirmNoButton.y + BUTTON_HEIGHT / 2 + 10);

        batch.end();
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

    public void reset() {
        currentState = State.MAIN;
        hoveredButton = -1;
    }

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        fontGenerator.dispose();
        titleFont.dispose();
        buttonFont.dispose();
        warningFont.dispose();
    }
}
