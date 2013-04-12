package com.gameforge.gamejam.pongout.core;

import playn.core.Surface;
import pythagoras.f.Rectangle;

public abstract class GameState {
	protected STATE state;
	protected PlayNRenderer renderer;

	public GameState(STATE state, PlayNRenderer renderer) {
		super();
		this.state = state;
		this.renderer = renderer;
	}

	public abstract void onEntry();

	public abstract void onExit();

	public abstract void paint(Surface surface, Rectangle renderRect,
			float alpha);

	public abstract void update(float delta);

	public enum STATE {
		PLAY, TEST
	}
}
