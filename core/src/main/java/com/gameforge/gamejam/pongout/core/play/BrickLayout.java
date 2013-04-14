/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import playn.core.Json;
import playn.core.Json.Array;
import playn.core.PlayN;

import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Spatial;
import java.util.ArrayList;
import java.util.List;
import static playn.core.PlayN.*;
/**
 * 
 * @author sascha
 */
public class BrickLayout extends GroupNode<Spatial> {

    private static final int BASEX = 365;
    private static final int BASEY = 0;
    
    private static final String[] levels = {"[[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[3,2,2,1,2,1,2,1,2,1,2,1,2,1,2,2,3],[0,0,0,0,0,4,4,3,0,3,4,4,0,0,0,0,0],[0,1,2,0,1,0,4,3,0,3,4,0,1,0,2,1,0],[2,1,2,3,0,0,5,0,5,0,5,0,0,3,2,1,2],[0,2,3,0,4,0,3,5,0,5,3,0,4,0,3,2,0],[2,1,2,3,0,0,5,0,5,0,5,0,0,3,2,1,2],[0,1,2,0,1,0,4,3,0,3,4,0,1,0,2,1,0],[0,0,0,0,0,4,4,3,0,3,4,4,0,0,0,0,0],[3,2,2,1,2,1,2,1,2,1,2,1,2,1,2,2,3],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]"};
//    private static final String[] levels = {"[[1,1,1]]","[[2,2,2]]","[[1,0,1]]"};
    
    BrickWall brickWallPlayer1;
    BrickWall brickWallPlayer2;
    
    public BrickLayout() {
        respawn();
    }
    
    public final void respawn() {
        float roll = random()*levels.length;
        log().info("rolling field " + roll);
        String level = levels[(int)roll];
        
        Json.Array arr = PlayN
                .json()
                .parseArray(level);

        Brick brick;
        for (int i = 0; i < arr.length(); i++) {
            Array row = arr.getArray(i);
            for (int j = 0; j < row.length(); j++) {
                int hitpoints = row.getInt(j);
                if (hitpoints > 0) {
                    brick = new Brick(j, i, BASEX, BASEY, hitpoints);
                    addChild(brick);
                }
            }
        }
        
        
    }
    
    List<Spatial> getBricks() {
        return getChildren();
    }

}
