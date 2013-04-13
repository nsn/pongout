package com.gameforge.gamejam.pongout.core;

import playn.core.Surface;
import pythagoras.f.Rectangle;

public abstract class GameState {
	protected STATE state;
	protected PlayNRenderer renderer;
    protected PongOut pongOut;

	public GameState(STATE state, PlayNRenderer renderer, PongOut pongOut) {
		super();
		this.state = state;
		this.renderer = renderer;
        this.pongOut = pongOut;
	}

	public abstract void onEntry();

	public abstract void onExit();

	public abstract void paint(Surface surface, Rectangle renderRect,
			float alpha);

	public abstract void update(float delta);

	public enum STATE {
		PLAY, TEST, RESULT, LOADING
	}
}
