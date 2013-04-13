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
    public LoadingState(PlayNRenderer renderer, PongOut pongOut) {
        super(GameState.STATE.LOADING, renderer, pongOut);
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
        surface.setFillColor(Color.rgb(0, 0, 0));
        surface.fillRect(0, 0, 1280, 800);
    }

    @Override
    public void update(float delta) {
    }
        
}
