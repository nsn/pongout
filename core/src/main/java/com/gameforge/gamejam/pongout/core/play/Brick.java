/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import com.gameforge.gamejam.pongout.core.PongoutSprite;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Spatial;
import playn.core.Color;
import pythagoras.f.Vector;

/**
 *
 * @author sascha
 */
public class Brick extends GroupNode<Spatial> {
    
    private int hitpoints;
    PongoutSprite sprite;
    private static final int BRICK_HORIZONTAL_SPACING = 10;
    private static final int BRICK_VERTICAL_SPACING = 10;
    private static final int BRICK_HEIGHT = 60;
    private static final int BRICK_WIDTH = 30;
    private static final int BRICK_OFFSET = 485;
    
    Brick(float xpos, float ypos, float xoffset, float yoffset, int hitpoints) {
        this.hitpoints = hitpoints;
        sprite = PongoutSprite.create(BRICK_WIDTH, BRICK_HEIGHT, 0, BRICK_OFFSET);
        sprite.setDrawBoundary(true);
        sprite.setBoundaryColor(Color.rgb(255, 0, 255));
        sprite.setFrame(hitpoints-1);
        sprite.translate(new Vector(xpos * (BRICK_WIDTH + BRICK_HORIZONTAL_SPACING) + xoffset, ypos * (BRICK_HEIGHT + BRICK_VERTICAL_SPACING) + yoffset));
        addChild(sprite);        
    }
    
    public void removeHitpoint() {
        hitpoints -= 1;
        sprite.setFrame(hitpoints-1);
    }

    public boolean isBroken() {
        return hitpoints <= 0;
    }
}
