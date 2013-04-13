/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import com.gameforge.gamejam.pongout.core.GameState;
import com.gameforge.gamejam.pongout.core.PlayNRenderer;
import com.gameforge.gamejam.pongout.core.PongOut;
import playn.core.Color;
import static playn.core.PlayN.log;
import playn.core.Surface;
import pythagoras.f.Rectangle;

/**
 *
 * @author sascha
 */
public class StartState extends GameState {

    public StartState(PlayNRenderer renderer, PongOut pongOut) {
        super(STATE.START, renderer, pongOut);
    }

    @Override
    public void onEntry() {
        log().info("Enter StartState");
    }

    @Override
    public void onExit() {
        log().info("Exit StartState");
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
