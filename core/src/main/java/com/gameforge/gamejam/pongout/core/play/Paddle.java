package com.gameforge.gamejam.pongout.core.play;

import playn.core.Color;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.PongoutSprite;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Spatial;

public class Paddle extends GroupNode<Spatial> {

    public static final int PADDLE_WIDTH = 30;
    public static final int TOP_OFFSET = 0;
    public static final int BOTTOM_OFFSET = 30;
    public static final int TOP_HEIGHT = 30;
    public static final int BOTTOM_HEIGHT = 30;
    public static final int MIDDLE_HEIGHT_DEFAULT = 30;
    public static final int MIDDLE_OFFSET = 60;
    public static final int BOTTOM_TRANSLATION_DEFAULT = 60;
    public static final int MIDDLE_TRANSLATION_DEFAULT = 30;
    public static final int PADDLE_SPEED = 1;
    public static final int BASEFRAME = 0;
    private final PongoutSprite top;
    private PongoutSprite bottom;
    private PongoutSprite middle;
    private UserInput userInput;
    private int frameModifier;

    Paddle(UserInput input, int frameModifier) {
        this.frameModifier = frameModifier;
        top = PongoutSprite.create(PADDLE_WIDTH, TOP_HEIGHT, 0, TOP_OFFSET);
        top.setDrawBoundary(false);
        top.setBoundaryColor(Color.rgb(255, 0, 0));
        top.setFrame(BASEFRAME+frameModifier);
        addChild(top);

        bottom = PongoutSprite.create(PADDLE_WIDTH, BOTTOM_HEIGHT, 0, BOTTOM_OFFSET);
        bottom.setDrawBoundary(false);
        bottom.setBoundaryColor(Color.rgb(0, 0, 255));
        bottom.translate(new Vector(0, BOTTOM_TRANSLATION_DEFAULT));
        bottom.setFrame(BASEFRAME+frameModifier);
        addChild(bottom);

        middle = PongoutSprite.create(PADDLE_WIDTH, MIDDLE_HEIGHT_DEFAULT, 0, MIDDLE_OFFSET);
        middle.translate(new Vector(0, MIDDLE_TRANSLATION_DEFAULT));
        middle.setDrawBoundary(false);
        middle.setBoundaryColor(Color.rgb(0, 255, 0));
        middle.setFrame(BASEFRAME+frameModifier);
        addChild(middle);

        setDrawBoundary(false);
        setBoundaryColor(Color.rgb(255, 0, 255));
        userInput = input;
    }

    public void setSize(float size) {
        getChildren().remove(middle);
        middle = PongoutSprite.create(PADDLE_WIDTH, size, 0, MIDDLE_OFFSET);
        middle.translate(new Vector(0, MIDDLE_TRANSLATION_DEFAULT));
        middle.setDrawBoundary(false);
        middle.setBoundaryColor(Color.rgb(0, 255, 0));
        middle.setFrame(BASEFRAME+frameModifier);

        bottom.setTranslation(new Vector(0, TOP_HEIGHT + size));

        addChild(middle);
    }
    
    public void setSpeed(float speed) {
        setSpeed(speed);
    }
    
    @Override
    public void update(float deltams) {
        super.update(deltams);
        if(userInput.up) {
            translate(new Vector(0, -1*PADDLE_SPEED*deltams));
        }
        if(userInput.down) {
            translate(new Vector(0, PADDLE_SPEED*deltams));
        }        
    }
    
    public void setFrame(int frame) {
        top.setFrame(frame+frameModifier);
        middle.setFrame(frame+frameModifier);
        bottom.setFrame(frame+frameModifier);
    }
}
