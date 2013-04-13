/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import com.google.gson.Gson;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Spatial;

/**
 *
 * @author sascha
 */
public class BrickLayout extends GroupNode<Spatial> {

    private static final int BASEX = 400;
    private static final int BASEY = 100;

    public BrickLayout() {

        Gson gson = new Gson();
        int[][] brickLayout = gson.fromJson("[[1,1,1,1,1],[1,1,1,1,1],[1,1,1,1,1],[1,1,1,1,1],[1,1,1,1,1]]", int[][].class);
        //int[][] brickLayout = gson.fromJson("[[5]]", int[][].class);

        for (int i = 0; i < brickLayout.length; i++) {
            int[] row = brickLayout[i];
            for (int j = 0; j < row.length; j++) {
                int hitpoints = row[j];
                if(hitpoints > 0) {
                    Brick brick = new Brick(j, i, BASEX, BASEY, hitpoints);
                    addChild(brick);                    
                }
            }
        }

    }
    
}
