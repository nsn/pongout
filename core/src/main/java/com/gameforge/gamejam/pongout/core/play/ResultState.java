/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import com.gameforge.gamejam.pongout.core.GameState;
import com.gameforge.gamejam.pongout.core.PlayNRenderer;
import com.gameforge.gamejam.pongout.core.PongOut;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Scene;
import com.nightspawn.sg.Spatial;
import playn.core.Color;
import playn.core.ImageLayer;
import playn.core.Surface;
import pythagoras.f.Rectangle;
import static playn.core.PlayN.*;

/**
 *
 * @author sascha
 */
public class ResultState extends GameState {

    ResultInput resultInput;
    private final Scene scene;
    private Result result;
    
    public ResultState(PlayNRenderer renderer, PongOut pongOut, int winner) {
        super(GameState.STATE.RESULT, renderer, pongOut);
        log().info("winner is " + winner);
        resultInput = new ResultInput();
        keyboard().setListener(resultInput);
		// init scene
		GroupNode<Spatial> root = new GroupNode<Spatial>();

		// init board
		result = new Result(winner);
		root.addChild(result);

		scene = new Scene(root);        
    }

    @Override
    public void onEntry() {
        log().info("Enter ResultState");
    }

    @Override
    public void onExit() {
        log().info("Exit ResultState");
    }

    @Override
    public void paint(Surface surface, Rectangle renderRect, float alpha) {
        //surface.setFillColor(Color.rgb(0, 0, 0));
        //surface.fillRect(0, 0, 1280, 730);
        renderer.render(scene, renderRect, surface, alpha);
    }

    @Override
    public void update(float delta) {
        scene.update(delta);
        if(resultInput.newGame) {
            pongOut.changeState(GameState.STATE.PLAY);
        }
    }
    
}
