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
    
    private static final String[] levels = {"[[0,0,0,0,1,2,3,3,3,3,2,1,0,0,0,0,0],[0,0,1,3,5,4,3,2,3,4,5,5,3,2,0,0,0],[0,2,4,3,1,0,0,0,0,0,2,4,5,5,3,0,0],[1,4,2,0,0,0,0,0,0,0,0,2,4,5,5,3,0],[3,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[3,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,2,0,0,0,1,2,3,4,4,5,5,5,5,5,4,2],[0,0,1,0,0,0,0,0,2,4,5,5,5,5,5,4,0],[0,1,0,0,0,0,0,0,0,1,4,5,5,5,4,0,0],[0,0,1,1,0,0,0,0,1,3,4,5,3,2,0,0,0],[0,0,0,0,1,2,3,3,3,4,2,1,0,0,0,0,0]]","[[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[3,2,2,1,2,1,2,1,2,1,2,1,2,1,2,2,3],[0,0,0,0,0,4,4,3,0,3,4,4,0,0,0,0,0],[0,1,2,0,1,0,4,3,0,3,4,0,1,0,2,1,0],[2,1,2,3,0,0,5,0,5,0,5,0,0,3,2,1,2],[0,2,3,0,4,0,3,5,0,5,3,0,4,0,3,2,0],[2,1,2,3,0,0,5,0,5,0,5,0,0,3,2,1,2],[0,1,2,0,1,0,4,3,0,3,4,0,1,0,2,1,0],[0,0,0,0,0,4,4,3,0,3,4,4,0,0,0,0,0],[3,2,2,1,2,1,2,1,2,1,2,1,2,1,2,2,3],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]]", "[[5,0,0,0,0,1,4,2,0,0,0,0,1,5,1,0,0],[5,0,0,0,0,0,3,0,0,0,0,0,0,4,0,0,0],[5,0,0,4,0,0,3,0,0,0,3,0,0,4,0,0,5],[5,0,0,4,0,0,3,0,0,2,3,0,0,4,0,0,5],[5,0,0,4,0,0,3,0,0,0,3,0,0,4,0,0,5],[5,0,0,4,1,1,3,2,2,2,3,1,1,4,0,0,5],[5,0,0,4,0,0,3,0,0,0,3,0,0,4,0,0,5],[5,0,0,4,0,0,3,2,0,0,3,0,0,4,0,0,5],[5,0,0,4,0,0,3,0,0,0,3,0,0,4,0,0,5],[0,0,0,4,0,0,0,0,0,0,3,0,0,0,0,0,5],[0,0,1,5,1,0,0,0,0,2,4,1,0,0,0,0,5]","[[0,0,0,0,1,2,2,2,2,2,2,2,1,0,0,0,0],[0,0,0,1,2,3,4,0,0,0,4,3,2,1,0,0,0],[0,0,1,2,3,4,0,0,1,0,0,4,3,2,1,0,0],[0,5,5,5,4,0,0,0,4,0,0,0,4,5,5,5,0],[0,0,0,0,0,0,0,4,5,4,0,0,0,0,0,0,0],[0,0,0,1,2,3,4,5,5,5,4,3,2,1,0,0,0],[0,0,0,0,0,0,0,4,5,4,0,0,0,0,0,0,0],[0,5,5,5,4,0,0,0,4,0,0,0,4,5,5,5,0],[0,0,1,2,3,4,0,0,1,0,0,4,3,2,1,0,0],[0,0,0,1,2,3,4,0,0,0,4,3,2,1,0,0,0],[0,0,0,0,1,2,2,2,2,2,2,2,1,0,0,0,0]]"};
//    private static final String[] levels = {"[[1,1,1]]","[[2,2,2]]","[[1,0,1]]"};
    
    BrickWall brickWallPlayer1;
    BrickWall brickWallPlayer2;
    
    public BrickLayout() {
        respawn(0);
    }
    
    public final void respawn(int roll) {
        String level = levels[roll];
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
    
    public final void respawn() {
        float roll = random()*levels.length;
        respawn((int)roll);
    }
    
    List<Spatial> getBricks() {
        return getChildren();
    }

}
