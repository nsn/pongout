package com.gameforge.gamejam.pongout.core.play;

import playn.core.Color;
import playn.core.Surface;
import pythagoras.f.Rectangle;

import com.gameforge.gamejam.pongout.core.GameState;
import com.gameforge.gamejam.pongout.core.PlayNRenderer;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Scene;
import com.nightspawn.sg.Spatial;

public class PlayState extends GameState {
	private final Scene scene;

	public PlayState(PlayNRenderer renderer) {
		super(GameState.STATE.PLAY, renderer);

		GroupNode<Spatial> root = new GroupNode<Spatial>();

		scene = new Scene(root);
	}

	@Override
	public void onEntry() {
	}

	@Override
	public void onExit() {
	}

	@Override
	public void paint(Surface surface, Rectangle renderRect, float alpha) {
		surface.clear();
		surface.setFillColor(Color.rgb(0, 0, 0));
		surface.fillRect(0.0f, 0.0f, renderRect.width, renderRect.height);
		renderer.render(scene, renderRect, surface);
	}

	@Override
	public void update(float delta) {
		scene.update(delta);
	}

}
