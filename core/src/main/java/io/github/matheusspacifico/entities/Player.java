package io.github.matheusspacifico.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.matheusspacifico.utils.Constants;

public class Player {
    private float x;
    private float y;

    // Animation textures
    private Texture stillTexture;
    private Texture walk2Texture;
    private Texture walk3Texture;

    // Animation state
    private static final float FRAME_DURATION = 0.12f;
    private float animationTimer;
    private int currentFrame;
    private boolean isMoving;
    private boolean facingLeft;

    public Player() {
        this.x = (Gdx.graphics.getWidth() - Constants.PLAYER_SIZE) / 2f;
        this.y = (Gdx.graphics.getHeight() - Constants.PLAYER_SIZE) / 2f;

        // Load animation frames
        this.stillTexture = new Texture("player-still.png");
        this.walk2Texture = new Texture("player-walk-2.png");
        this.walk3Texture = new Texture("player-walk-3.png");

        this.animationTimer = 0f;
        this.currentFrame = 0;
        this.isMoving = false;
        this.facingLeft = false;
    }

    public void update(float delta) {
        float speed = Constants.PLAYER_SPEED;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            speed = Constants.PLAYER_SPRINT_SPEED;
        }

        // Track movement for animation
        boolean movingThisFrame = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += speed * delta;
            movingThisFrame = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= speed * delta;
            movingThisFrame = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= speed * delta;
            movingThisFrame = true;
            facingLeft = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += speed * delta;
            movingThisFrame = true;
            facingLeft = false;
        }

        x = Math.max(0, Math.min(x, Gdx.graphics.getWidth() - Constants.PLAYER_SIZE));
        y = Math.max(0, Math.min(y, Gdx.graphics.getHeight() - Constants.PLAYER_SIZE));

        // Update animation
        isMoving = movingThisFrame;
        if (isMoving) {
            animationTimer += delta;
            if (animationTimer >= FRAME_DURATION) {
                animationTimer = 0f;
                // Cycle: 0 (still) -> 1 (walk2) -> 2 (walk3) -> 1 (walk2) -> repeat
                currentFrame = (currentFrame + 1) % 4;
            }
        } else {
            // Reset to still frame when not moving
            animationTimer = 0f;
            currentFrame = 0;
        }
    }

    private Texture getCurrentTexture() {
        if (!isMoving) {
            return stillTexture;
        }
        // Frame sequence: still -> walk2 -> walk3 -> walk2 -> (repeat)
        switch (currentFrame) {
            case 0: return stillTexture;
            case 1: return walk2Texture;
            case 2: return walk3Texture;
            case 3: return walk2Texture;
            default: return stillTexture;
        }
    }

    public void render(SpriteBatch batch) {
        Texture texture = getCurrentTexture();

        if (facingLeft) {
            // Flip horizontally by using negative width
            batch.draw(texture,
                x + Constants.PLAYER_SIZE, y,  // Offset x when flipping
                -Constants.PLAYER_SIZE, Constants.PLAYER_SIZE);
        } else {
            batch.draw(texture, x, y, Constants.PLAYER_SIZE, Constants.PLAYER_SIZE);
        }
    }

    public void dispose() {
        stillTexture.dispose();
        walk2Texture.dispose();
        walk3Texture.dispose();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
