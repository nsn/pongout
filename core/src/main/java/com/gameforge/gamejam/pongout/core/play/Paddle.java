package com.gameforge.gamejam.pongout.core.play;

import playn.core.Color;
import pythagoras.f.AffineTransform;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.PongoutSprite;
import com.nightspawn.sg.BoundingRectangle;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Spatial;

public class Paddle extends GroupNode<Spatial> {
    public static final int FRONT_TOP = 0;
    public static final int FRONT_BOTTOM = 1;
    public static final int BACK_TOP = 2;
    public static final int BACK_BOTTOM = 3;
    public static final int PADDLE_WIDTH = 30;
    public static final int TOP_OFFSET = 0;
    public static final int BOTTOM_OFFSET = 30;
    public static final int TOP_HEIGHT = 30;
    public static final int BOTTOM_HEIGHT = 30;
    public static final int MIDDLE_HEIGHT_DEFAULT = 60;
    public static final int MIDDLE_OFFSET = 60;
    public static final int BOTTOM_TRANSLATION_DEFAULT = 90;
    public static final int MIDDLE_TRANSLATION_DEFAULT = 30;

    public static final int BASEFRAME = 0;
    public static final float CURVE = 0.5f;
    public static final float BOUNCE_SUB = 10.0f;
    public static final float BOUNCE_ADD = 15.0f;
    public static final int SIZE_INCREASE = 60;
    public static final int MAX_SIZE = 240;
    private final PongoutSprite top;
    private PongoutSprite bottom;
    private PongoutSprite middle;
    private UserInput userInput;
    private int frameModifier;
    private float paddleSpeed = 1.0f;
    Player player;

    Paddle(UserInput input, int frameModifier, Player p) {
        this.player = p;
        this.frameModifier = frameModifier;
        top = PongoutSprite.create(PADDLE_WIDTH, TOP_HEIGHT, 0, TOP_OFFSET);
        top.setDrawBoundary(false);
        top.setBoundaryColor(Color.rgb(255, 0, 0));
        top.setFrame(BASEFRAME + frameModifier);
        addChild(top);

        bottom = PongoutSprite.create(PADDLE_WIDTH, BOTTOM_HEIGHT, 0,
                BOTTOM_OFFSET);
        bottom.setDrawBoundary(false);
        bottom.setBoundaryColor(Color.rgb(0, 0, 255));
        bottom.translate(new Vector(0, BOTTOM_TRANSLATION_DEFAULT));
        bottom.setFrame(BASEFRAME + frameModifier);
        addChild(bottom);

        middle = PongoutSprite.create(PADDLE_WIDTH, MIDDLE_HEIGHT_DEFAULT, 0,
                MIDDLE_OFFSET);
        middle.translate(new Vector(0, MIDDLE_TRANSLATION_DEFAULT));
        middle.setDrawBoundary(false);
        middle.setBoundaryColor(Color.rgb(0, 255, 0));
        middle.setFrame(BASEFRAME + frameModifier);
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
        middle.setFrame(BASEFRAME + frameModifier);

        bottom.setTranslation(new Vector(0, TOP_HEIGHT + size));

        addChild(middle);
    }

    public void increaseSize() {
        float currentSize = middle.getWorldBound().height;
        if (currentSize < MAX_SIZE) {
            setSize(currentSize + SIZE_INCREASE);
        }
    }

    public void setSpeed(float speed) {
        paddleSpeed = speed;
    }

    public void setCurrentPowerup(PowerUp.TYPE type) {
        switch (type) {
        case MULTIBALL:
            setFrame(8);
            break;
        case ENLARGE:
            setFrame(6);
            break;
        case SPEED:
            setFrame(2);
            break;
        case CONTROLS:
            setFrame(12);
            break;
        case BOMB:
            setFrame(4);
            break;
        }
    }

    public void setFrame(int frame) {
        top.setFrame(frame + frameModifier);
        middle.setFrame(frame + frameModifier);
        bottom.setFrame(frame + frameModifier);
    }

    @Override
    public void update(float deltams) {
        super.update(deltams);
        float velocity = 0.0f;
        AffineTransform transform = new AffineTransform();
        if (userInput.up) {
            velocity = -1;
        }
        if (userInput.down) {
            velocity = 1;
        }
        velocity *= paddleSpeed * deltams;
        transform.setTy(velocity);

        BoundingRectangle ob = getWorldBound();
        BoundingRectangle nb = ob.translate(transform);

        if (nb.minY() <= Board.TOP) {
            transform.setTy(Board.TOP - ob.minY());
        }
        if (nb.maxY() >= Board.BOTTOM) {
            transform.setTy(Board.BOTTOM - ob.maxY());
        }

        transform(transform);
    }

    public Vector[] getBound() {
        Rectangle r = getBounceRectangle();
        boolean left = frameModifier > 0;
        float xOffset = left ? 0.0f : r.width;

        Vector[] bounds = new Vector[4];
        bounds[FRONT_TOP] = new Vector(r.minX() + xOffset, r.minY());
        bounds[FRONT_BOTTOM] = new Vector(r.minX() + xOffset, r.maxY());
        bounds[BACK_TOP] = new Vector(r.maxX() - xOffset, r.minY() - BOUNCE_ADD);
        bounds[BACK_BOTTOM] = new Vector(r.maxX() - xOffset, r.maxY()
                + BOUNCE_ADD);

        return bounds;
    }

    public Rectangle getBounceRectangle() {
        BoundingRectangle r = getWorldBound();
        return new Rectangle(r.x, r.y + BOUNCE_SUB, r.width, r.height - 2
                * BOUNCE_SUB);
    }

    public UserInput getUserInput() {
        return userInput;
    }

}
