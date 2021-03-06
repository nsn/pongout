/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import static playn.core.PlayN.*;
import com.gameforge.gamejam.pongout.core.PongoutSprite;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Spatial;
import pythagoras.f.Vector;

/**
 *
 * @author sascha
 */
public class PowerUp extends GroupNode<Spatial>{

    private TYPE type;
    PongoutSprite sprite;
    private static final int ANIMATION_FRAMES = 4;
    private static final int ANIMATION_FRAME_DURATION = 150;
    private int deltaCounter;
    
    public enum TYPE {
        SPEED,BOMB,ENLARGE,MULTIBALL,BALLSPEED,CONTROLS,REPAIR
    }
    
    public PowerUp(TYPE type, Vector position) {
        
        deltaCounter = 0;
        
        sprite = PongoutSprite.create(30, 60, 0, getOffset(type));
        sprite.translate(position);
        
        this.type = type;
        
        addChild(sprite);
        
    }

    public TYPE getType() {
        return type;
    }
    
    private int getOffset(TYPE type) {
        int offset = 0;
        switch(type) {
            case SPEED:
                offset = 545;
                break;
            case BOMB:
                offset = 605;
                break;
            case ENLARGE:
                offset = 665;
                break;
            case MULTIBALL:
                offset = 725;
                break;
            case BALLSPEED:
                offset = 785;
                break;
            case CONTROLS:
                offset = 845;
                break;
            case REPAIR:
                offset = 905;
                break;
        }
        return offset;
    }
    
    public void update(float delta) {
        super.update(delta);
        deltaCounter += delta;
        if(deltaCounter>=ANIMATION_FRAME_DURATION) {
            sprite.setFrame((sprite.getFrame()+1) % ANIMATION_FRAMES);
            deltaCounter = 0;
        }
    }
    
}
