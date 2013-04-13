/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import com.gameforge.gamejam.pongout.core.PongoutSprite;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Node;
import com.nightspawn.sg.Spatial;
import java.util.ArrayList;
import java.util.List;
import playn.core.Color;
import pythagoras.f.Vector;

/**
 *
 * @author sascha
 */
public class Scores extends GroupNode<Spatial> {
    public static final int PLAYER1_POINTS_OFFSET = 100;
    public static final int PLAYER2_POINTS_OFFSET = 1000;
    
    List<PongoutSprite> player1Points;
    List<PongoutSprite> player2Points;
    
    Scores() {
        player1Points = new ArrayList<PongoutSprite>();
        player2Points = new ArrayList<PongoutSprite>();
        for (int i = 0; i < 5; i++) {
            PongoutSprite player1Heart = PongoutSprite.create(30, 30, 0, 0);
            player1Heart.translate(new Vector(40 * i + PLAYER1_POINTS_OFFSET, 0));
            player1Heart.setBoundaryColor(Color.rgb(255, 0, 255));
            player1Points.add(player1Heart);
            addChild(player1Heart);
            
            PongoutSprite player2Heart = PongoutSprite.create(30, 30, 0, 0);
            player2Heart.translate(new Vector(40 * i + PLAYER2_POINTS_OFFSET, 0));
            player2Heart.setBoundaryColor(Color.rgb(255, 0, 255));
            player2Points.add(player2Heart);
            addChild(player2Heart);
            
        }
    }
    
    public void removePointForPlayer1() {
        if(player1Points.size() > 0) {
            PongoutSprite lastHeart = player1Points.get(player1Points.size()-1);
            player1Points.remove(lastHeart);
            getChildren().remove(lastHeart);
        }
    }

    public void removePointForPlayer2() {
        if(player2Points.size() > 0) {
            PongoutSprite lastHeart = player2Points.get(player2Points.size()-1);
            player2Points.remove(lastHeart);
            getChildren().remove(lastHeart);
        }
    }
    
}
