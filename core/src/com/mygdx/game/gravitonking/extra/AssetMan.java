package com.mygdx.game.gravitonking.extra;

import static com.mygdx.game.gravitonking.extra.Utils.ATLAS_MAP;
import static com.mygdx.game.gravitonking.extra.Utils.BACKGROUND_IMAGE;
import static com.mygdx.game.gravitonking.extra.Utils.BG_MUSIC_1;
import static com.mygdx.game.gravitonking.extra.Utils.BG_MUSIC_2;
import static com.mygdx.game.gravitonking.extra.Utils.BOOM;
import static com.mygdx.game.gravitonking.extra.Utils.GO_BACKGROUND_IMAGE;
import static com.mygdx.game.gravitonking.extra.Utils.KING1;
import static com.mygdx.game.gravitonking.extra.Utils.KING2;
import static com.mygdx.game.gravitonking.extra.Utils.KING3;
import static com.mygdx.game.gravitonking.extra.Utils.KING4;
import static com.mygdx.game.gravitonking.extra.Utils.KING_INV_1;
import static com.mygdx.game.gravitonking.extra.Utils.KING_INV_2;
import static com.mygdx.game.gravitonking.extra.Utils.KING_INV_3;
import static com.mygdx.game.gravitonking.extra.Utils.KING_INV_4;
import static com.mygdx.game.gravitonking.extra.Utils.NORMAL_FONT_FNT;
import static com.mygdx.game.gravitonking.extra.Utils.NORMAL_FONT_PNG;
import static com.mygdx.game.gravitonking.extra.Utils.TITLE_FONT_FNT;
import static com.mygdx.game.gravitonking.extra.Utils.TITLE_FONT_PNG;
import static com.mygdx.game.gravitonking.extra.Utils.WALL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetMan {
    private AssetManager assetManager;
    private TextureAtlas textureAtlas;

    public AssetMan(){
        this.assetManager = new AssetManager();

        assetManager.load(ATLAS_MAP, TextureAtlas.class); // Carga los recursos
        assetManager.load(BG_MUSIC_1, Music.class);
        assetManager.load(BG_MUSIC_2, Music.class);
        assetManager.load(BOOM, Sound.class);
        assetManager.finishLoading(); // Bloquea la ejecución hasta que se hayan cargado todos los recursos

        this.textureAtlas = assetManager.get(ATLAS_MAP); // Coge el atlas
    }

    /** IMAGEN DE FONDO **/
    public TextureRegion getBackground(){
        return this.textureAtlas.findRegion(BACKGROUND_IMAGE);
    }
    public TextureRegion getGOBackground() { return this.textureAtlas.findRegion(GO_BACKGROUND_IMAGE);}

    /** TEXTURA DEL REY **/
    // Modo normal (gravedad normal)
    public Animation<TextureRegion> getKingAnimation_normal(){
        return new Animation<TextureRegion>(0.25f,
                textureAtlas.findRegion(KING1),
                textureAtlas.findRegion(KING2),
                textureAtlas.findRegion(KING3),
                textureAtlas.findRegion(KING4));
    }
    // Modo inverso (gravedad invertida)
    public Animation<TextureRegion> getKingAnimation_inverted(){
        return new Animation<TextureRegion>(0.25f,
                textureAtlas.findRegion(KING_INV_1),
                textureAtlas.findRegion(KING_INV_2),
                textureAtlas.findRegion(KING_INV_3),
                textureAtlas.findRegion(KING_INV_4));
    }

    /** TEXTURA DE LAS PAREDES **/
    public TextureRegion getWallTR(){
        return this.textureAtlas.findRegion(WALL);
    }

    /** MÚSICA **/
    public Music getMusic_1(){
        return this.assetManager.get(BG_MUSIC_1);
    }
    public Music getMusic_2(){
        return this.assetManager.get(BG_MUSIC_2);
    }

    /** SONIDOS **/
    public Sound getDeathSound(){ return this.assetManager.get(BOOM);}

    /** FUENTES **/
    public BitmapFont getFont_normal(){
        return new BitmapFont(Gdx.files.internal(NORMAL_FONT_FNT),Gdx.files.internal(NORMAL_FONT_PNG), false);
    }

    public BitmapFont getFont_title(){
        return new BitmapFont(Gdx.files.internal(TITLE_FONT_FNT),Gdx.files.internal(TITLE_FONT_PNG), false);
    }
}
