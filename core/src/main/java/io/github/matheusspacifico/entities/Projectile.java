package io.github.matheusspacifico.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.matheusspacifico.utils.Constants;

public class Projectile implements Entity {
    private float x;
    private float y;
    private float velocityX;
    private float velocityY;
    private boolean active;
    private Rectangle bounds;

    private static Texture texture;

    public Projectile(float startX, float startY, float targetX, float targetY) {
        this.x = startX;
        this.y = startY;
        this.active = true;
        this.bounds = new Rectangle(
            startX - Constants.PROJECTILE_SIZE / 2f,
            startY - Constants.PROJECTILE_SIZE / 2f,
            Constants.PROJECTILE_SIZE,
            Constants.PROJECTILE_SIZE
        );

        float dirX = targetX - startX;
        float dirY = targetY - startY;
        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);

        if (length > 0) {
            this.velocityX = (dirX / length) * Constants.PROJECTILE_SPEED;
            this.velocityY = (dirY / length) * Constants.PROJECTILE_SPEED;
        } else {
            this.velocityX = 0;
            this.velocityY = Constants.PROJECTILE_SPEED;
        }

        if (texture == null) {
            texture = new Texture("note-bullet.png");
        }
    }

    @Override
    public void update(float delta) {
        if (!active) return;

        x += velocityX * delta;
        y += velocityY * delta;

        // Update bounds to match position
        bounds.setPosition(x - Constants.PROJECTILE_SIZE / 2f, y - Constants.PROJECTILE_SIZE / 2f);

        if (x < -Constants.PROJECTILE_SIZE || x > Gdx.graphics.getWidth() + Constants.PROJECTILE_SIZE ||
            y < -Constants.PROJECTILE_SIZE || y > Gdx.graphics.getHeight() + Constants.PROJECTILE_SIZE) {
            active = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!active) return;
        batch.draw(texture, x - Constants.PROJECTILE_SIZE / 2f, y - Constants.PROJECTILE_SIZE / 2f,
            Constants.PROJECTILE_SIZE, Constants.PROJECTILE_SIZE);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public boolean isDestroyed() {
        return !active;
    }

    @Override
    public void dispose() {
        // Texture is static and managed separately via disposeTexture()
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public static void disposeTexture() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
    }
}
