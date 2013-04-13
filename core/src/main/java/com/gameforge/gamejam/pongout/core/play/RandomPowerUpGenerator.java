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

    public static PowerUp generate(Brick brick) {
        int roll = new Random().nextInt(PowerUp.TYPE.values().length);
        PowerUp powerUp = new PowerUp(PowerUp.TYPE.values()[roll], new Vector(brick.sprite.getWorldBound().minX(), brick.sprite.getWorldBound().minY()-50));     
        //PowerUp powerUp = new PowerUp(PowerUp.TYPE.CONTROLS, new Vector(brick.sprite.getWorldBound().minX(), brick.sprite.getWorldBound().minY()-50));     
        return powerUp;
    }
    
}
