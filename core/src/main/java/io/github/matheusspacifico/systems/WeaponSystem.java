package io.github.matheusspacifico.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.matheusspacifico.entities.Player;
import io.github.matheusspacifico.entities.Projectile;
import io.github.matheusspacifico.utils.Constants;
import io.github.matheusspacifico.world.GameWorld;

public class WeaponSystem {
    private int currentAmmo;
    private boolean isReloading;
    private float reloadTimer;
    private float fireTimer;
    private GameWorld world;
    private Viewport viewport;
    private BitmapFont font;

    public WeaponSystem(GameWorld world, Viewport viewport) {
        this.world = world;
        this.viewport = viewport;
        this.currentAmmo = Constants.MAGAZINE_SIZE;
        this.isReloading = false;
        this.reloadTimer = 0f;
        this.fireTimer = 0f;
        this.font = new BitmapFont();
    }

    public void update(float delta, Player player) {
        fireTimer -= delta;

        if (isReloading) {
            reloadTimer -= delta;
            if (reloadTimer <= 0) {
                currentAmmo = Constants.MAGAZINE_SIZE;
                isReloading = false;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && !isReloading && currentAmmo < Constants.MAGAZINE_SIZE) {
            startReload();
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && canShoot()) {
            shoot(player);
        }
    }

    private boolean canShoot() {
        return !isReloading && currentAmmo > 0 && fireTimer <= 0;
    }

    private void shoot(Player player) {
        float playerCenterX = player.getX() + Constants.PLAYER_SIZE / 2f;
        float playerCenterY = player.getY() + Constants.PLAYER_SIZE / 2f;

        // Convert screen coordinates to world coordinates
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        Projectile projectile = new Projectile(playerCenterX, playerCenterY, mousePos.x, mousePos.y);
        world.spawnProjectile(projectile);

        currentAmmo--;
        fireTimer = Constants.FIRE_RATE;

        if (currentAmmo <= 0) {
            startReload();
        }
    }

    private void startReload() {
        isReloading = true;
        reloadTimer = Constants.RELOAD_TIME;
    }

    public void renderUI(SpriteBatch batch) {
        String ammoText;
        if (isReloading) {
            ammoText = "RELOADING...";
        } else {
            ammoText = currentAmmo + " / " + Constants.MAGAZINE_SIZE;
        }

        font.draw(batch, ammoText, 10, 30);
    }

    public void dispose() {
        Projectile.disposeTexture();
        font.dispose();
    }
}
