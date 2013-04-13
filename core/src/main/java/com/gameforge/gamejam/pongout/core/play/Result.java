/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Spatial;
import com.nightspawn.sg.Sprite;
import playn.core.Image;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import static playn.core.PlayN.*;
/**
 *
 * @author sascha
 */
public class Result extends GroupNode<Spatial> {

    public Result(int winner) {
        Sprite<Image> sprite;
        log().info("winner in result is " + winner);
        if (winner == 1) {
            Image img = ImageStore.getInstance().getImage("player1wins");
            log().info("p1img " + img.height());
            sprite = new Sprite<Image>(img, new Dimension(1280, 768));
        } else {
            Image img = ImageStore.getInstance().getImage("player2wins");
            log().info("p2img " + img.height());
            sprite = new Sprite<Image>(img, new Dimension(1280, 768));
        }
        sprite.setDrawBoundary(true);
        addChild(sprite);
    }
}
