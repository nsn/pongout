package com.gameforge.gamejam.pongout.core;

import static playn.core.PlayN.graphics;
import playn.core.Game;
import playn.core.ImmediateLayer;
import playn.core.Surface;
import pythagoras.f.Rectangle;

import com.gameforge.gamejam.pongout.core.play.PlayState;

public class PongOut implements Game {
	public static final int SCREENWIDTH = 1280;
	public static final int SCREENHEIGHT = 768;
	private GameState currentState;
	private PlayNRenderer renderer;
	private float frameAlpha;

	@Override
	public void init() {
		graphics().ctx().setSize(SCREENWIDTH, SCREENHEIGHT);
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
		changeState(GameState.STATE.PLAY);
	}

	public void changeState(GameState.STATE newState) {
		switch (newState) {
		case PLAY:
			changeState(new PlayState(renderer));
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
