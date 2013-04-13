package com.gameforge.gamejam.pongout.core;

import static com.gameforge.gamejam.pongout.core.GameState.STATE.RESULT;
import com.gameforge.gamejam.pongout.core.play.ImageStore;
import com.gameforge.gamejam.pongout.core.play.LoadingState;
import static playn.core.PlayN.graphics;
import playn.core.Game;
import playn.core.ImmediateLayer;
import playn.core.Surface;
import pythagoras.f.Rectangle;

import com.gameforge.gamejam.pongout.core.play.PlayState;
import com.gameforge.gamejam.pongout.core.play.ResultState;
import playn.core.AssetWatcher;
import playn.core.AssetWatcher.Listener;
import playn.core.Image;
import static playn.core.PlayN.assets;

public class PongOut implements Game {
	public static final int SCREENWIDTH = 1280;
	public static final int SCREENHEIGHT = 768;
	private GameState currentState;
	private PlayNRenderer renderer;
	private float frameAlpha = 1.0f;

	@Override
	public void init() {
		graphics().ctx().setSize(SCREENWIDTH, SCREENHEIGHT);

        final Image backgroundImage = assets().getImage("images/background.jpg");
        final Image spritesheetImage = assets().getImage("images/spritesheet.png");
        graphics().rootLayer().add(graphics().createImageLayer(backgroundImage));

		ImmediateLayer gameLayer = graphics().createImmediateLayer(SCREENWIDTH,
				SCREENHEIGHT, new ImmediateLayer.Renderer() {
					@Override
					public void render(Surface surface) {
						currentState.paint(surface, new Rectangle(0.0f, 0.0f,
								SCREENWIDTH, SCREENHEIGHT), frameAlpha);
					}
				});

		graphics().rootLayer().add(gameLayer);

        renderer = new PlayNRenderer();
        
        AssetWatcher assetWatcher = new AssetWatcher(new Listener() {

            @Override
            public void done() {
                ImageStore.getInstance().addImage("spritesheet", spritesheetImage);
                ImageStore.getInstance().addImage("background", backgroundImage);
        		changeState(GameState.STATE.PLAY);
            }

            @Override
            public void error(Throwable e) {
            }
            
        });
        assetWatcher.add(backgroundImage);
        assetWatcher.add(spritesheetImage);
        assetWatcher.start();
        
        changeState(GameState.STATE.LOADING);
        
	}

	public void changeState(GameState.STATE newState) {
		switch (newState) {
		case PLAY:
			changeState(new PlayState(renderer, this));
			break;
        case RESULT:
            changeState(new ResultState(renderer, this));
            break;
        case LOADING:
            changeState(new LoadingState(renderer, this));
            break;            
		default:
			throw new IllegalArgumentException("unable to switch to "
					+ newState);
		}
	}

	private void changeState(GameState state) {
		if (currentState != null) {
			currentState.onExit();
		}
		currentState = state;
		currentState.onEntry();
	}

	@Override
	public void paint(float alpha) {
		frameAlpha = alpha;
	}

	@Override
	public void update(float delta) {
		currentState.update(delta);
	}

	@Override
	public int updateRate() {
		return 25;
	}
}
