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

/**
 * 
 * @author sascha
 */
public class BrickLayout extends GroupNode<Spatial> {

    private static final int BASEX = 400;
    private static final int BASEY = 100;

    public BrickLayout() {
        Json.Array arr = PlayN
                .json()
                .parseArray(
                        "[[1,1,1,1,1],[1,1,1,1,1],[1,1,1,1,1],[1,1,1,1,1],[1,1,1,1,1]]");

        for (int i = 0; i < arr.length(); i++) {
            Array row = arr.getArray(i);
            for (int j = 0; j < row.length(); j++) {
                int hitpoints = row.getInt(j);
                if (hitpoints > 0) {
                    Brick brick = new Brick(j, i, BASEX, BASEY, hitpoints);
                    addChild(brick);
                }
            }
        }

    }

}
