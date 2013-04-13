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
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.log;
import playn.core.Surface;
import pythagoras.f.Rectangle;

/**
 *
 * @author sascha
 */
public class LoadingState extends GameState{
    
    ResultInput resultInput;
    Scene scene;
    private LoadingScreen loadingScreen;
    
    public LoadingState(PlayNRenderer renderer, PongOut pongOut) {
        super(GameState.STATE.RESULT, renderer, pongOut);
        resultInput = new ResultInput();
        keyboard().setListener(resultInput);
		// init scene
		GroupNode<Spatial> root = new GroupNode<Spatial>();

		// init board
        loadingScreen = new LoadingScreen();
        root.addChild(loadingScreen);
        
		scene = new Scene(root);        

    }

    @Override
    public void onEntry() {
        log().info("Enter LoadingState");
    }

    @Override
    public void onExit() {
        log().info("Exit LoadingState");
    }

    @Override
    public void paint(Surface surface, Rectangle renderRect, float alpha) {
//        surface.setFillColor(Color.rgb(0, 0, 0));
//        surface.fillRect(0, 0, 1280, 800);
        renderer.render(scene, renderRect, surface, alpha);
    }

    @Override
    public void update(float delta) {
        scene.update(delta);
        if(resultInput.newGame) {
            log().info("starting game...");
            pongOut.changeState(GameState.STATE.PLAY);
        }
    }
        
}
