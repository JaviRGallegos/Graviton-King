package com.mygdx.game.gravitonking.windows;

import static com.mygdx.game.gravitonking.extra.Utils.WORLD_HEIGHT;
import static com.mygdx.game.gravitonking.extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.gravitonking.MainGame;

public class GameOverScreen extends BaseScreen{
    private Stage stage;
    private Image background;

    public GameOverScreen(MainGame mainGame) {
        super(mainGame);
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage = new Stage(fitViewport);
    }

    public void addBackground(){
        this.background = new Image(mainGame.assetManager.getGOBackground());
        this.background.setPosition(0, 0);
        this.background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage.addActor(this.background);
    }

    @Override
    public void show() {
        addBackground();
    }

    @Override
    public void render(float delta) {
        stage.draw(); // Dibujamos el nuevo escenario
        stage.act();
        boolean retry = Gdx.input.justTouched(); // Lanzamos el evento cuando se toque estando la pantalla de Game Over
        if(retry){
            this.stage.addAction(Actions.sequence(
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            mainGame.setScreen(new GameScreen(mainGame)); // Volvemos al juego
                        }
                    })
            ));
        }
    }

    @Override
    public void hide() {
        super.hide();
        this.background.remove();
    }

    @Override
    public void dispose() {
        super.dispose();
    }





    public void render(){

    }
}
