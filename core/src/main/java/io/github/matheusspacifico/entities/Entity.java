package io.github.matheusspacifico.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Base interface for all game entities.
 * Provides common contract for update, render, and collision.
 */
public interface Entity {

    /**
     * Update entity state each frame.
     * @param delta Time since last frame in seconds
     */
    void update(float delta);

    /**
     * Render the entity.
     * @param batch SpriteBatch for drawing
     */
    void render(SpriteBatch batch);

    /**
     * Get the entity's bounding box for collision detection.
     * @return Rectangle representing entity bounds
     */
    Rectangle getBounds();

    /**
     * Check if entity should be removed from the world.
     * @return true if entity should be destroyed
     */
    boolean isDestroyed();

    /**
     * Get entity's X position.
     */
    float getX();

    /**
     * Get entity's Y position.
     */
    float getY();

    /**
     * Clean up resources when entity is removed.
     */
    void dispose();
}
