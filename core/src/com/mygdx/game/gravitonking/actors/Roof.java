package com.mygdx.game.gravitonking.actors;

import static com.mygdx.game.gravitonking.extra.Utils.USER_FLOOR;
import static com.mygdx.game.gravitonking.extra.Utils.USER_ROOF;
import static com.mygdx.game.gravitonking.extra.Utils.WORLD_HEIGHT;
import static com.mygdx.game.gravitonking.extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Roof {
    private World world;
    private Fixture fixture;
    private Vector2 position;
    private Body body;

    public Roof(World world){
        this.world = world;
        this.position = new Vector2(0f, WORLD_HEIGHT);

        createBody();
        createFixture();
    }

    public void createBody(){
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(this.position);

        bodyDef.type = BodyDef.BodyType.StaticBody; // Estático porque no le afecta la gravedad y no se mueve

        this.body = this.world.createBody(bodyDef);
    }

    public void createFixture(){
        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(WORLD_WIDTH, 0.01f);

        this.fixture = this.body.createFixture(rectangle, 8);
        this.fixture.setUserData(USER_ROOF);
        rectangle.dispose();
    }
}