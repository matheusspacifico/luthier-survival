package io.github.matheusspacifico.utils;

public final class Constants {
    // Screen
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;

    // Sprite scale factor (compensates for higher resolution)
    // At 800x600 with 32px sprites, player was ~4% of screen width
    // At 1920x1080, we scale up to maintain similar proportion
    public static final float SPRITE_SCALE = 2.5f;

    // Player
    public static final int PLAYER_SIZE = (int) (32 * SPRITE_SCALE);
    public static final float PLAYER_SPEED = 150f * SPRITE_SCALE;
    public static final float PLAYER_SPRINT_SPEED = 250f * SPRITE_SCALE;

    // Projectile
    public static final int PROJECTILE_SIZE = (int) (8 * SPRITE_SCALE);
    public static final float PROJECTILE_SPEED = 400f * SPRITE_SCALE;

    // Weapon
    public static final int MAGAZINE_SIZE = 6;
    public static final float RELOAD_TIME = 1.5f;
    public static final float FIRE_RATE = 0.15f;

    // Crosshair
    public static final int CROSSHAIR_SIZE = (int) (16 * SPRITE_SCALE);

    // House
    public static final int HOUSE_WIDTH = (int) (96 * SPRITE_SCALE);
    public static final int HOUSE_HEIGHT = (int) (96 * SPRITE_SCALE);
    public static final int HOUSE_INITIAL_HP = 100;

    private Constants() {}
}
