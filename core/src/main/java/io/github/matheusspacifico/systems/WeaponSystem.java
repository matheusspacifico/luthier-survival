package io.github.matheusspacifico.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private BitmapFont font;

    public WeaponSystem(GameWorld world) {
        this.world = world;
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

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        Projectile projectile = new Projectile(playerCenterX, playerCenterY, mouseX, mouseY);
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
