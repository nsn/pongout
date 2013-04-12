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
    private final PongoutSprite top;
    private PongoutSprite bottom;
    private PongoutSprite middle;

    Paddle() {
        top = PongoutSprite.create(PADDLE_WIDTH, TOP_HEIGHT, 0, TOP_OFFSET);
        top.setDrawBoundary(true);
        top.setBoundaryColor(Color.rgb(255, 0, 0));
        addChild(top);

        bottom = PongoutSprite.create(PADDLE_WIDTH, BOTTOM_HEIGHT, 0, BOTTOM_OFFSET);
        bottom.setDrawBoundary(true);
        bottom.setBoundaryColor(Color.rgb(0, 0, 255));
        bottom.translate(new Vector(0, BOTTOM_TRANSLATION_DEFAULT));
        addChild(bottom);

        middle = PongoutSprite.create(PADDLE_WIDTH, MIDDLE_HEIGHT_DEFAULT, 0, MIDDLE_OFFSET);
        middle.translate(new Vector(0, MIDDLE_TRANSLATION_DEFAULT));
        middle.setDrawBoundary(true);
        middle.setBoundaryColor(Color.rgb(0, 255, 0));
        addChild(middle);

        //setSize(150);

        setDrawBoundary(true);
        setBoundaryColor(Color.rgb(255, 0, 255));
        translate(new Vector(100, 100));
    }

    public void setSize(float size) {
        getChildren().remove(middle);
        middle = PongoutSprite.create(PADDLE_WIDTH, size, 0, MIDDLE_OFFSET);
        middle.translate(new Vector(0, MIDDLE_TRANSLATION_DEFAULT));
        middle.setDrawBoundary(true);
        middle.setBoundaryColor(Color.rgb(0, 255, 0));

        bottom.setTranslation(new Vector(0, TOP_HEIGHT + size));

        addChild(middle);
    }
}
