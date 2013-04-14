/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import playn.core.Key;
import playn.core.Keyboard;

/**
 *
 * @author sascha
 */
public class ResultInput implements Keyboard.Listener {

    boolean newGame = false;
    boolean randomLevel = false;
    
    public ResultInput() {
        super();
        
    }

    @Override
    public void onKeyDown(Keyboard.Event event) {
        if(event.key() == Key.ENTER) {
            newGame = true;
        }
        if(event.key() == Key.L) {
            randomLevel = true;
        }
    }

    @Override
    public void onKeyTyped(Keyboard.TypedEvent event) {
    }

    @Override
    public void onKeyUp(Keyboard.Event event) {
    }
    
}
