/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import java.util.Random;
import pythagoras.f.Vector;

/**
 *
 * @author sascha
 */
public class RandomPowerUpGenerator {

    private static final PowerUp.TYPE[] types = {PowerUp.TYPE.SPEED};
    
    public static PowerUp generate(Brick brick) {
        int roll = new Random().nextInt(types.length);
        PowerUp powerUp = new PowerUp(types[roll], new Vector(brick.sprite.getWorldBound().minX(), brick.sprite.getWorldBound().minY()-50));        
        return powerUp;
    }
    
}
