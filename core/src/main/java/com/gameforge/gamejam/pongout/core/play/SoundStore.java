/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import java.util.HashMap;
import java.util.Map;
import playn.core.Image;
import playn.core.Sound;

/**
 *
 * @author sascha
 */
public class SoundStore {
    private static SoundStore instance = null;
    private Map<String, Sound> sounds;

    public SoundStore() {
        sounds = new HashMap<String,Sound>();
    }
    
    public static SoundStore getInstance() {
        if(instance == null) {
            instance = new SoundStore();
        }
        return instance;
    }

    public void addSound(String name, Sound img) {
        sounds.put(name, img);
    }
    
    public Sound getSound(String name) {
        return sounds.get(name);
    }    
}
