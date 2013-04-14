package com.gameforge.gamejam.pongout.core.play;

import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.log;
import playn.core.Color;
import playn.core.Key;
import playn.core.Surface;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.GameState;
import com.gameforge.gamejam.pongout.core.InputListenerList;
import com.gameforge.gamejam.pongout.core.PlayNRenderer;
import com.gameforge.gamejam.pongout.core.PongOut;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Scene;
import com.nightspawn.sg.Spatial;

public class PlayState extends GameState {
    private final Scene scene;
    private final InputListenerList inputListeners;
    private final UserInput player1Input;
    private final UserInput player2Input;
    private Board board;

    public PlayState(PlayNRenderer renderer, PongOut pongOut) {
        super(GameState.STATE.PLAY, renderer, pongOut);

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
        if (board.scores.player1Points.size() > board.scores.player2Points
                .size()) {
            pongOut.setLastWinner(1);
        } else {
            pongOut.setLastWinner(2);
        }
    }

    @Override
    public void paint(Surface surface, Rectangle renderRect, float alpha) {
        renderer.render(scene, renderRect, surface, alpha);

        surface.setTransform(1, 0, 0, 1, 0, 0);

        renderRay(surface, board.draw[0], board.draw[1], Color.rgb(255, 0, 0));
        renderRay(surface, board.draw[2], board.draw[3], Color.rgb(0, 255, 0));
        surface.setFillColor(Color.rgb(255, 255, 255));
        surface.fillRect(board.draw[4].x - 1, board.draw[4].y - 1, 3, 3);
    }

    private void renderRay(Surface surface, Vector p1, Vector p2, int color) {
        surface.setFillColor(color);
        surface.drawLine(p1.x, p1.y, p2.x, p2.y, 1);
    }

    @Override
    public void update(float delta) {
        scene.update(delta);
        if (board.scores.gameOver()) {
            pongOut.changeState(GameState.STATE.RESULT);
        }
    }

}
