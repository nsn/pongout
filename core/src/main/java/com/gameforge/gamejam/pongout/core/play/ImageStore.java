/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gameforge.gamejam.pongout.core.play;

import java.util.HashMap;
import java.util.Map;
import playn.core.Image;

/**
 *
 * @author sascha
 */
public class ImageStore {
    
    private static ImageStore instance = null;
    private Map<String, Image> images;

    public ImageStore() {
        images = new HashMap<String,Image>();
    }
    
    public static ImageStore getInstance() {
        if(instance == null) {
            instance = new ImageStore();
        }
        return instance;
    }

    public void addImage(String name, Image img) {
        images.put(name, img);
    }
    
    public Image getImage(String name) {
        return images.get(name);
    }
    
}
