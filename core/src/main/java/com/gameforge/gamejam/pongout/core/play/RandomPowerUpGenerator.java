/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import java.util.Random;

/**
 *
 * @author sascha
 */
public class RandomPowerUpGenerator {

    private static final PowerUp.TYPE[] types = {PowerUp.TYPE.GLUE};
    
    public static PowerUp generate(Brick brick) {
        int roll = new Random().nextInt(types.length);
        PowerUp powerUp = new PowerUp(types[roll], brick.getWorldPosition());        
        return powerUp;
    }
    
}
