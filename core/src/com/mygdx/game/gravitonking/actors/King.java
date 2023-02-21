package com.mygdx.game.gravitonking.actors;

import static com.mygdx.game.gravitonking.extra.Utils.USER_KING;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.gravitonking.extra.AssetMan;

public class King extends Actor {

    private static final int STATE_NORMAL = 1;
    private static final int STATE_INVERTED = -1;
    private static final int STATE_DEAD = 0;

    public int state;


    private Animation<TextureRegion> kingAnimation;

    private Animation<TextureRegion> kingNormalAnimation;
    private Animation<TextureRegion> kingInvAnimation;
    private final Vector2 position;
    private float stateTime;
    private World world;

    private Body body;
    private Fixture fixture;
    private float currentRotation;

    public King(World world, Animation<TextureRegion> normalAnimation, Animation<TextureRegion> invAnimation, Vector2 position){
        this.kingNormalAnimation = normalAnimation;
        this.kingInvAnimation = invAnimation;
        this.kingAnimation = normalAnimation;
        this.position = position;
        this.world = world;
        this.state = STATE_NORMAL;
        this.stateTime = 0f;
        this.currentRotation = 0f;


        createBody();
        createFixture();
    }

    private void createBody(){
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(this.position);

        bodyDef.type = BodyDef.BodyType.DynamicBody; // Dinámico para que le afecte la gravedad que le pondremos al mundo

        this.body = this.world.createBody(bodyDef);
    }

    private void createFixture(){ // Crear la hitbox del personaje
        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(0.6f / 2, 1f / 2);

        this.fixture = this.body.createFixture(rectangle, 8);
        this.fixture.setUserData(USER_KING);
        this.fixture.setFriction(0);

        rectangle.dispose();

    }

    private void animation(){
        if(this.state == STATE_NORMAL){
            this.kingAnimation = this.kingNormalAnimation;
        } else if (this.state == STATE_INVERTED) {
            this.kingAnimation = this.kingInvAnimation;
        }
    }

    // Sobrecarga de los métodos draw y act
    @Override
    public void act(float delta) {
        boolean inversion = Gdx.input.justTouched();
        if(inversion && this.state != 0) { // Cambiar la gravedad, el estado y los sprites
            this.body.setLinearVelocity(0, 0); // Queremos que el personaje meta un frenazo para evitar curvas de movimiento e inercia que se irían incrementando con el tiempo
            this.world.setGravity(new Vector2( 0, this.world.getGravity().y * -1.0f));
            this.state = this.state * -1;
            animation();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(body.getPosition().x - 0.35f, body.getPosition().y - 0.5f); // Ponemos el origen de la imagen en la esquina inferior izqda de la hitbox para que se correspondan
        batch.draw(this.kingAnimation.getKeyFrame(stateTime, true), getX(), getY(), 0f, 0f,  0.7f,1f, 1, 1, currentRotation ); // Pintamos la imagen tras haberla colocado bien con respecto a la hitbox
        stateTime += Gdx.graphics.getDeltaTime();
    }

    // Método para eliminar de la tarjeta gráfica
    public void detach(){
        this.body.destroyFixture(this.fixture);
        this.world.destroyBody(this.body);
    }

    public void dead(){
        this.state = STATE_DEAD;
    }
}
