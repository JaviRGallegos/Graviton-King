package com.mygdx.game.gravitonking;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.gravitonking.extra.AssetMan;
import com.mygdx.game.gravitonking.windows.BaseScreen;
import com.mygdx.game.gravitonking.windows.GameOverScreen;
import com.mygdx.game.gravitonking.windows.GameScreen;

public class MainGame extends Game {
	public Screen gameOverScreen;
	protected BaseScreen baseScreen;

	private GameScreen gameScreen;
	private GameOverScreen goScreen;
	public AssetMan assetManager;

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		this.assetManager = new AssetMan();
		this.gameScreen = new GameScreen(this);
		this.goScreen = new GameOverScreen(this);
		setScreen(this.gameScreen);
	}
}
