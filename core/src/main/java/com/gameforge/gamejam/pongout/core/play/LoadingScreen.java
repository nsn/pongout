/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Spatial;
import com.nightspawn.sg.Sprite;
import playn.core.Image;
import static playn.core.PlayN.log;
import pythagoras.f.Dimension;
import static playn.core.PlayN.*;

/**
 *
 * @author sascha
 */
public class LoadingScreen extends GroupNode<Spatial>{

    public LoadingScreen() {
        Image img = ImageStore.getInstance().getImage("opening");
        log().info("loading opening screen " + img.width());
        Sprite<Image> sprite = new Sprite<Image>(img, new Dimension(1280, 768));
        addChild(sprite);    
    }
    
}
