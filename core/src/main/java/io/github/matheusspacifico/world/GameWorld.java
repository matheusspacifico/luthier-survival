package io.github.matheusspacifico.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import io.github.matheusspacifico.entities.Player;
import io.github.matheusspacifico.entities.Projectile;
import io.github.matheusspacifico.systems.WeaponSystem;
import io.github.matheusspacifico.utils.Constants;

import java.util.Iterator;

public class GameWorld implements Disposable {

    private Player player;

    private Array<Projectile> projectiles;

    private WeaponSystem weaponSystem;

    private int currentDay;

    public GameWorld() {
        projectiles = new Array<>();

        currentDay = 1;

        initializeEntities();
        initializeSystems();
    }

    private void initializeEntities() {
        float playerStartX = Constants.SCREEN_WIDTH / 2f;
        float playerStartY = Constants.SCREEN_HEIGHT / 4f;
        player = new Player(playerStartX, playerStartY);
    }

    private void initializeSystems() {
        weaponSystem = new WeaponSystem(this);
    }

    public void update(float delta) {
        player.update(delta);
        weaponSystem.update(delta, player);

        for (Projectile projectile : projectiles) {
            projectile.update(delta);
        }

        cleanupEntities();
    }

    public void render(SpriteBatch batch) {
        player.render(batch);

        for (Projectile projectile : projectiles) {
            projectile.render(batch);
        }
    }

    public void renderUI(SpriteBatch batch) {
        weaponSystem.renderUI(batch);
    }

    private void cleanupEntities() {
        Iterator<Projectile> projIter = projectiles.iterator();
        while (projIter.hasNext()) {
            Projectile p = projIter.next();
            if (p.isDestroyed()) {
                p.dispose();
                projIter.remove();
            }
        }
    }

    public void spawnProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Projectile> getProjectiles() {
        return projectiles;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void advanceDay() {
        currentDay++;
        if (currentDay > 7) {
            // Demo complete - will be handled by game flow system
        }
    }

    @Override
    public void dispose() {
        player.dispose();
        weaponSystem.dispose();

        for (Projectile p : projectiles) {
            p.dispose();
        }
        projectiles.clear();
    }
}
