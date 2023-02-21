package com.mygdx.game.gravitonking.windows;

import static com.mygdx.game.gravitonking.extra.Utils.BOOM;
import static com.mygdx.game.gravitonking.extra.Utils.SCREEN_HEIGHT;
import static com.mygdx.game.gravitonking.extra.Utils.SCREEN_WIDTH;
import static com.mygdx.game.gravitonking.extra.Utils.USER_FLOOR;
import static com.mygdx.game.gravitonking.extra.Utils.USER_KING;
import static com.mygdx.game.gravitonking.extra.Utils.USER_ROOF;
import static com.mygdx.game.gravitonking.extra.Utils.USER_WALL;
import static com.mygdx.game.gravitonking.extra.Utils.WORLD_HEIGHT;
import static com.mygdx.game.gravitonking.extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.gravitonking.MainGame;
import com.mygdx.game.gravitonking.actors.Floor;
import com.mygdx.game.gravitonking.actors.King;
import com.mygdx.game.gravitonking.actors.Roof;
import com.mygdx.game.gravitonking.actors.Wall;


public class GameScreen extends BaseScreen implements ContactListener {
    private King king;
    private Stage stage;
    private Image Background;

    private World world;
    private Image background;


    private  static final float WALL_SPAWN_TIME = 2f;
    private float wallCreationTime;

    private Array<Wall> arrayWalls;
    Wall wall;

    private Array<Music> bgMusic; // Música de fondo
    private Music activeSong;
    private Sound deathSound;

    // Herramientas de depuración
    //private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;

    // Puntuación
    private BitmapFont score;

    private int score_meter;

    public GameScreen(MainGame mainGame) {
        super(mainGame);

        this.world = new World(new Vector2(0,-9.8f), false); // Ponemos que el personaje siga siendo afectado por la gravedad aun habiendo tocado techo o suelo
        this.world.setContactListener(this);
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage = new Stage(fitViewport);
        this.wallCreationTime = 0f;
        this.arrayWalls = new Array();

        this.bgMusic = new Array();
        this.bgMusic.add(this.mainGame.assetManager.getMusic_1());
        this.bgMusic.add(this.mainGame.assetManager.getMusic_2());
        this.deathSound = this.mainGame.assetManager.getDeathSound();

        this.camera = (OrthographicCamera) this.stage.getCamera();
        //this.debugRenderer = new Box2DDebugRenderer();
        
        prepareScore();
        score_meter = 0;
    }

    @Override
    public void show() {
        addBackground();
        addKing();
        addFloor();
        addRoof();


        int index = MathUtils.random(0, 1);
        this.bgMusic.get(index).setLooping(true);
        this.bgMusic.get(index).play();
        this.activeSong = this.bgMusic.get(index);

    }

    @Override
    public void render(float delta) {
        if(this.king.state == 0){
            mainGame.setScreen(new GameOverScreen(mainGame));
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        addWalls(0.02f);

        // Le pasamos al escenario los datos de la cámara del mundo, para que vuelva a representar tod~o en función del tamaño de este
        this.stage.getBatch().setProjectionMatrix(camera.combined);
        this.stage.act();
        this.world.step(delta,6,2);
        this.stage.draw();

        //this.debugRenderer.render(this.world, this.camera.combined);
        removeWalls();

        // Cargamos la matriz de proyección con los datos de la cámara de la fuente para que proyecte el texto con las dimensiones en píxeles
        this.stage.getBatch().setProjectionMatrix(this.fontCamera.combined);
        this.stage.getBatch().begin();
        this.score.draw(this.stage.getBatch(), this.score_meter + "m", SCREEN_WIDTH - 150f, 450f);
        this.stage.getBatch().end();
        score_meter++;
    }

    @Override
    public void hide() {
        this.king.detach();
        this.king.remove();
        removeWalls();

        this.activeSong.pause();
        this.background.remove();

        this.stage.dispose();
        this.world.dispose();
    }

    @Override
    public void dispose() {
        this.stage.dispose();
        this.world.dispose();
    }

    public void addBackground(){
        this.background = new Image(mainGame.assetManager.getBackground());
        this.background.setPosition(0, 0);
        this.background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage.addActor(this.background);
    }

    public void addKing(){
        Animation<TextureRegion> kingSprite = mainGame.assetManager.getKingAnimation_normal();
        Animation<TextureRegion> kingInvSprite = mainGame.assetManager.getKingAnimation_inverted();
        this.king = new King(this.world, kingSprite, kingInvSprite, new Vector2(1f, 2f));
        this.stage.addActor(this.king);
    }

    public void addFloor(){
        Floor floor = new Floor(this.world);
    }

    public void addRoof(){
        Roof roof = new Roof(this.world);
    }

    public void addWalls(float interval){

        TextureRegion pipeDownTexture = mainGame.assetManager.getWallTR();

        if(king.state == -1 || king.state == 1) {
            // Acumulamos interval hasta que llegue al tiempo que hemos establecido para que cree el próximo muro
            this.wallCreationTime += interval;
            // Si el tiempo acumulado es mayor que el establecido, se crea un muro nuevo
            if(this.wallCreationTime >= WALL_SPAWN_TIME) {
                this.wallCreationTime -= WALL_SPAWN_TIME;
                float posRandomY = MathUtils.random(0f, 4f);
                // Cambiamos la coordenada x para que se cree fuera de la pantalla
                Wall wall = new Wall(this.world, pipeDownTexture, new Vector2(8f, posRandomY)); //Posición de la pared
                arrayWalls.add(wall);
                this.stage.addActor(wall);
            }
        }
    }

    public void removeWalls(){
        for (Wall wall : this.arrayWalls) {
            // Si el mundo no está actualizando la física en este momento y la pared en cuestión se encuentra fuera de la pantalla,
            // eliminamos sus recursos y dicha pared del escenario
            if(!world.isLocked()) {
                if(wall.isOutOfScreen()) {
                    wall.detach();
                    wall.remove();

                    // Finalmente, eliminamos la pared del array
                    arrayWalls.removeValue(wall,false);
                }
            }
        }
    }

    private void prepareScore(){

        this.score = this.mainGame.assetManager.getFont_normal() ;
        this.score.getData().setScale(0.45f, 0.25f);

        // Creamos la cámara, le damos el tamaño de la pantalla en píxeles y finalmente lo actualizamos
        this.fontCamera = new OrthographicCamera();
        this.fontCamera.setToOrtho(false, SCREEN_WIDTH,SCREEN_HEIGHT);
        this.fontCamera.update();
    }

    private boolean areColliding(Contact contact, Object objA, Object objB){
        return (contact.getFixtureA().getUserData().equals(objA) && contact.getFixtureB().getUserData().equals(objB)) ||
                (contact.getFixtureA().getUserData().equals(objB) && contact.getFixtureB().getUserData().equals(objA));
    }

    @Override
    public void beginContact(Contact contact) {
        if (areColliding(contact, USER_KING, USER_WALL)) {
            // Si el rey se estampa de boca contra una pared, cambiamos el estado a muerto y lanzamos el sonido de muerte
            king.dead();
            // Paramos la música
            this.activeSong.stop();
            this.deathSound.play();
            // Recorremos el array de paredes para parar las que se encuentren creadas en este momento
            for (Wall wall : arrayWalls) {
                wall.stopWalls();
            }

            // Se lanza la secuencia de acciones,cuya última será el pasar a la ventana de GameOverScreen
            this.stage.addAction(Actions.sequence(
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            mainGame.setScreen(new GameOverScreen(mainGame));
                        }
                    })
            ));
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}