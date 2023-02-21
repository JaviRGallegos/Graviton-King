package com.mygdx.game.gravitonking.actors;

import static com.mygdx.game.gravitonking.extra.Utils.USER_WALL;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.gravitonking.extra.Utils;

public class Wall extends Actor {

    private static final float WALL_WIDTH = 0.3f;
    private static final float WALL_HEIGHT = 2.25f;

    // Texturas, fixture & mundo
    private TextureRegion wallTR;
    private Body body;
    private Fixture wallFixture;
    private World world;

    // Constructor

    public Wall(World world,TextureRegion wallTR, Vector2 position) {
        this.wallTR = wallTR;
        this.world = world;

        createBodyWall(position);
        createFixture();
    }

    private void createBodyWall(Vector2 position) {
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.KinematicBody; // Kinematico porque no le afecta la gravedad
        body = world.createBody(def);
        body.setLinearVelocity(-6f, 0);
    }

    private void createFixture() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WALL_WIDTH / 2, WALL_HEIGHT / 2);

        this.wallFixture = body.createFixture(shape, 8);
        this.wallFixture.setUserData(USER_WALL);
        shape.dispose();
    }

    public void stopWalls(){
        this.body.setLinearVelocity(0, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public boolean isOutOfScreen(){
        return this.body.getPosition().x <= -2f;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(this.body.getPosition().x - (WALL_WIDTH / 2), this.body.getPosition().y - (WALL_HEIGHT / 2) );
        batch.draw(this.wallTR, getX(), getY(), WALL_WIDTH, WALL_HEIGHT);
    }

    /** Liberar recursos **/
    public void detach(){
        body.destroyFixture(wallFixture);
        world.destroyBody(body);
    }

}
