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

    public BrickWall(int xpos) {
        Brick brick;
        for (int i = 0; i < 11; i++) {
            log().info("adding brick to brickwall");
            brick = new Brick(xpos, i*Brick.BRICK_HEIGHT, 3);
            brick.isWallBrick = true;
            addChild(brick);
        }
    }
}
