package io.github.matheusspacifico.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.matheusspacifico.utils.Constants;

public class Player {
    private float x;
    private float y;
    private Texture texture;

    public Player() {
        this.x = (Constants.SCREEN_WIDTH - Constants.PLAYER_SIZE) / 2f;
        this.y = (Constants.SCREEN_HEIGHT - Constants.PLAYER_SIZE) / 2f;
        this.texture = new Texture("player-still.png");
    }

    public void update(float delta) {
        float speed = Constants.PLAYER_SPEED;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            speed = Constants.PLAYER_SPRINT_SPEED;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += speed * delta;
        }

        x = Math.max(0, Math.min(x, Constants.SCREEN_WIDTH - Constants.PLAYER_SIZE));
        y = Math.max(0, Math.min(y, Constants.SCREEN_HEIGHT - Constants.PLAYER_SIZE));
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, Constants.PLAYER_SIZE, Constants.PLAYER_SIZE);
    }

    public void dispose() {
        texture.dispose();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
