package io.github.matheusspacifico;

import com.badlogic.gdx.Game;
import io.github.matheusspacifico.screens.GameScreen;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
