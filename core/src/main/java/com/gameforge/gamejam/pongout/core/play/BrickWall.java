/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Spatial;
import static playn.core.PlayN.log;

/**
 *
 * @author sascha
 */
public class BrickWall extends GroupNode<Spatial>{
    
    private static final int BRICKWALL_MAX_HP = 3;
    
    Brick[] brickLine = new Brick[11];
    private int xpos;

    public BrickWall(int xpos) {
        this.xpos = xpos;
        Brick brick;
        for (int i = 0; i < 11; i++) {
            log().info("adding brick to brickwall");
            brick = new Brick(xpos, i*Brick.BRICK_HEIGHT, BRICKWALL_MAX_HP);
            brick.isWallBrick = true;
            brickLine[i] = brick;
            addChild(brick);
        }
    }
    
    public void repair() {

        for (Spatial spatial : children) {
            Brick b = (Brick) spatial;
            if(b.hitpoints < BRICKWALL_MAX_HP) {
                b.hitpoints += 1;
            }
        }
        for (int i = 0; i < 11; i++) {
            Brick b = brickLine[i];
            if(b.isBroken()) {
                b = new Brick(xpos, i*Brick.BRICK_HEIGHT, 1);
                b.isWallBrick = true;
                brickLine[i] = b;
                addChild(b);
            }
        }
        
    }
}
