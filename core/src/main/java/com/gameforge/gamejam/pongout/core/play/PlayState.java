package com.gameforge.gamejam.pongout.core.play;

import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.log;
import playn.core.Color;
import playn.core.Key;
import playn.core.Surface;
import pythagoras.f.Rectangle;

import com.gameforge.gamejam.pongout.core.GameState;
import com.gameforge.gamejam.pongout.core.InputListenerList;
import com.gameforge.gamejam.pongout.core.PlayNRenderer;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Scene;
import com.nightspawn.sg.Spatial;

public class PlayState extends GameState {
	private final Scene scene;
	private final InputListenerList inputListeners;
	private final UserInput player1Input;
	private final UserInput player2Input;

	public PlayState(PlayNRenderer renderer) {
		super(GameState.STATE.PLAY, renderer);

		// init input
		inputListeners = new InputListenerList();

		Key[] p1UpKeys = { Key.A, Key.W };
		Key[] p1DownKeys = { Key.S, Key.D };
		Key[] p1ActionKeys = { Key.Q, Key.E, Key.F, Key.C, Key.V };
		player1Input = new UserInput(p1UpKeys, p1DownKeys, p1ActionKeys);
		inputListeners.addListener(player1Input);

		Key[] p2UpKeys = { Key.UP, Key.LEFT, Key.K8, Key.K6 };
		Key[] p2DownKeys = { Key.DOWN, Key.RIGHT, Key.K4, Key.K5 };
		Key[] p2ActionKeys = { Key.K0, Key.K7, Key.K9, Key.K1, Key.K3,
				Key.SLASH, Key.COLON, Key.UNDERSCORE };
		player2Input = new UserInput(p2UpKeys, p2DownKeys, p2ActionKeys);
		inputListeners.addListener(player2Input);

		// init scene
		GroupNode<Spatial> root = new GroupNode<Spatial>();

		scene = new Scene(root);
	}

	@Override
	public void onEntry() {
		log().info("PlayState enter");
		keyboard().setListener(inputListeners);
	}

	@Override
	public void onExit() {
		log().info("PlayState exit");
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
