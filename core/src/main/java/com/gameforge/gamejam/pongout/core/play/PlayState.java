package com.gameforge.gamejam.pongout.core.play;

import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.log;
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
	private Board board;

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

		// init board
		board = new Board(player1Input, player2Input);
		board.spawnBall();
		root.addChild(board);

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
		renderer.render(scene, renderRect, surface, alpha);

		// surface.setTransform(1, 0, 0, 1, 0, 0);
		// surface.drawLine(0, Board.OFFSET.y, Board.DIMENSION.width,
		// Board.OFFSET.y, 1);
		// surface.setFillColor(Color.rgb(255, 0, 0));
		// surface.fillRect(board.draw[0].x, board.draw[0].y, 5, 5);
		// surface.setFillColor(Color.rgb(0, 255, 0));
		// surface.fillRect(board.draw[1].x, board.draw[1].y, 5, 5);
		// surface.setFillColor(Color.rgb(0, 0, 255));
		// surface.fillRect(board.draw[2].x, board.draw[2].y, 5, 5);
	}

	@Override
	public void update(float delta) {
		scene.update(delta);
	}

}
