/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Spatial;
import pythagoras.f.Vector;

/**
 *
 * @author sascha
 */
public class PowerUp extends GroupNode<Spatial>{

    private TYPE type;
    
    public enum TYPE {
        GLUE
    }
    
    public PowerUp(TYPE type, Vector position) {
    }

    public TYPE getType() {
        return type;
    }
    
}
