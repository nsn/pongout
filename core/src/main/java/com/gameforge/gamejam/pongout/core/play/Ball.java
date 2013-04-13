package com.gameforge.gamejam.pongout.core.play;

import static playn.core.PlayN.log;

import java.util.Iterator;

import playn.core.Color;
import pythagoras.f.AffineTransform;
import pythagoras.f.Dimension;
import pythagoras.f.Lines;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.PongoutSprite;
import com.nightspawn.sg.BoundingRectangle;
import com.nightspawn.sg.Spatial;

public class Ball extends GameObject {
    private static final float INITIAL_SPEED = 12f; // pixels per msec
    private static final Dimension DIMENSION = new Dimension(20, 20);
    private static final Vector OFFSET = new Vector(0, 420);
    private float speed; // pixels/ms
    Vector direction;
    AffineTransform transform;
    BoundingRectangle ob;
    BoundingRectangle nb;
    Vector op;
    Vector np;
    Board board;
    Player lastBounce = Player.NONE;
    private PongoutSprite sprite;

    Ball(Board board, Vector direction) {
        this.board = board;
        sprite = new PongoutSprite(DIMENSION, OFFSET);
        addChild(sprite);

        speed = INITIAL_SPEED;
        this.direction = direction.normalize();

        setDrawBoundary(true);
        setBoundaryColor(Color.rgb(0, 0, 255));
    }

    private boolean bouncePowerup(PowerUp powerup) {
        BoundingRectangle r = powerup.sprite.getWorldBound();
        boolean hitSomething = false;
        if (op.x > r.maxX()) {
//            log().info(String.format("right from brick %f,%f,%f,%f", r.maxX(), r.minY(), r.maxX(), r.maxY()));
            if (Lines.linesIntersect(op.x, op.y, np.x, np.y, r.maxX(), r.minY(), r.maxX(), r.maxY())) {
//                log().info("hit brick from right");
                direction.x *= -1;
                transform.setTx(ob.minX() - r.x);
                hitSomething = true;
            }
        } else if (op.y > r.maxY()) {
//            log().info("bottom from brick");
            if (Lines.linesIntersect(op.x, op.y, np.x, np.y, r.minX(), r.maxY(), r.maxX(), r.maxY())) {
//                log().info("hit brick from bottom");
                direction.y *= -1;
                transform.setTy(ob.minY() - r.y);
                hitSomething = true;
            }
        } else if (op.x < r.minX()) {
//            log().info("left from brick");
            if (Lines.linesIntersect(op.x, op.y, np.x, np.y, r.minX(), r.minY(), r.minX(), r.maxY())) {
//                log().info("hit brick from left");
                direction.x *= -1;
                transform.setTx(r.x - ob.maxX());
                hitSomething = true;
            }
        } else if (op.y < r.minY()) {
//            log().info("top from brick");
            if (Lines.linesIntersect(op.x, op.y, np.x, np.y, r.minX(), r.minY(), r.maxX(), r.minY())) {
//                log().info("hit brick from top");
                direction.y *= -1;
                transform.setTy(r.y - ob.maxY());
                hitSomething = true;
            }
        }
        if (hitSomething) {
            board.collectPowerup(powerup, this);
        }
        return hitSomething;
    }
    
    private void bounceLine(Vector s, Vector e, float friction,
            Vector velocity, float curve) {
        Rectangle r = new Rectangle(s.x, s.y, e.x - s.x, e.y - s.y);
        float newX = 0.0f;
        if (Lines.linesIntersect(op.x, op.y, np.x, np.y, s.x, s.y, e.x, e.y)) {
            log().debug("intersect");
            Vector newDir = s.add(e.subtract(e)).crossLocal(direction);
            log().info("penis " + direction + "  <->  " + newDir);
            if (op.x > r.maxX()) {
                newX = op.x - r.maxX();
                log().debug("right " + newX);
            }
            if (op.x < r.minX()) {
                newX = r.minX() - op.x;
                log().debug("left " + newX);
            }
            if (op.y > r.maxY()) {
                // log().debug("top");
                // direction.y *= -1;
            }
            if (op.y < r.minX()) {
                // log().debug("bottom");
                // direction.y *= -1;
            }
            if (newX != 0.0f) {
                transform.setTx(newX);
                // left or right
                direction.x *= -1;
                // friction
                direction.y += friction * velocity.y;
                // curve
                float intersectY = np.y - s.y;
                float ratio = intersectY / (r.height * .5f) - 1;
                direction.y += curve * ratio;
            }
            // direction = newDir.normalize();

            board.draw[2] = s.clone();
            board.draw[3] = e.clone();
        }

    }

    private void bouncePaddle(Paddle paddle, boolean left) {
        Rectangle r = paddle.getBounceRectangle();
        float xOffset = left ? r.width : 0.0f;
        Vector ro = new Vector(r.minX() + xOffset, r.minY());
        Vector rd = new Vector(r.minX() + xOffset, r.maxY());
        bounceLine(ro, rd, Paddle.FRICTION, paddle.velocity, Paddle.CURVE);
    }

    private void bouncePaddleOld(Paddle paddle, boolean left) {
        Rectangle r = paddle.getBounceRectangle();
        float xOffset = left ? r.width : 0.0f;
        Vector ro = new Vector(r.minX() + xOffset, r.minY());
        Vector rd = new Vector(r.minX() + xOffset, r.maxY());

        if (Lines
                .linesIntersect(op.x, op.y, np.x, np.y, ro.x, ro.y, rd.x, rd.y)) {
            float newX;
            if (left) {
                newX = ob.minX() - r.x;
            } else {
                newX = r.x - ob.maxX();
            }
            transform.setTx(newX);

            // flat paddle
            direction.x *= -1;
            // friction
            direction.y += Paddle.FRICTION * paddle.velocity.y;
            // rounded paddle
            float intersectY = np.y - ro.y;
            float ratio = intersectY / (r.height * .5f) - 1;
            direction.y += Paddle.CURVE * ratio;

            direction.normalizeLocal();

            lastBounce = paddle.player;

            board.draw[2] = ro.clone();
            board.draw[3] = rd.clone();
        }
        if (op.y > rd.y) {

        }

    }

    public void update(float delta) {
        oldBoundingRectangle = getWorldBound().clone();

        Vector dist = direction.scale(speed);
        transform = new AffineTransform();
        transform.translate(dist.x, dist.y);

        ob = oldBoundingRectangle;
        nb = getWorldBound().translate(transform);

        op = new Vector(ob.center().x, ob.center().y);
        np = new Vector(nb.center().x, nb.center().y);

        // leaves play area
        if (op.x < Board.LEFT) {
            log().info("LEFT " + np.x);
            board.removeBall(this);
            board.scores.removePointForPlayer1();
            return;
        }
        if (op.x > Board.RIGHT) {
            board.removeBall(this);
            board.scores.removePointForPlayer2();
            return;
        }

        // hit upper or lower bounds
        if (nb.minY() <= Board.OFFSET.y) {
            float newY = ob.minY() - Board.OFFSET.y;
            transform.setTy(newY);
            direction.y *= -1;
        }
        if (nb.maxY() >= Board.BOTTOM) {
            float newY = nb.maxY() - Board.BOTTOM;
            transform.setTy(newY);
            direction.y *= -1;
        }

        // player paddles
        bouncePaddle(board.player1Paddle, true);
        bouncePaddle(board.player2Paddle, false);

        // bricks
        for (Iterator<Spatial> it = board.brickLayout.getChildren().iterator(); it
                .hasNext();) {
            Brick brick = (Brick) it.next();
            if (bounceBrick(brick)) {
                break;
            }
        }
        
        // powerups
        for (PowerUp powerUp : board.powerUps) {
            if(bouncePowerup(powerUp)) {
                break;
            }
        }

        board.draw[0] = op.clone();
        board.draw[1] = np.clone();
        // board.draw[2] = ro.clone();
        // board.draw[3] = rd.clone();
        // board.draw[4] = intersection.clone();

        transform(transform);

        super.update(delta);
    }

    public Point center() {
        return getWorldBound().center();
    }

    private boolean bounceBrick(Brick brick) {
        BoundingRectangle r = brick.sprite.getWorldBound();
        boolean hitSomething = false;
        if (op.x > r.maxX()) {
            // log().info(String.format("right from brick %f,%f,%f,%f",
            // r.maxX(), r.minY(), r.maxX(), r.maxY()));
            if (Lines.linesIntersect(op.x, op.y, np.x, np.y, r.maxX(),
                    r.minY(), r.maxX(), r.maxY())) {
                // log().info("hit brick from right");
                direction.x *= -1;
                transform.setTx(ob.minX() - r.x);
                hitSomething = true;
            }
        } else if (op.y > r.maxY()) {
            // log().info("bottom from brick");
            if (Lines.linesIntersect(op.x, op.y, np.x, np.y, r.minX(),
                    r.maxY(), r.maxX(), r.maxY())) {
                // log().info("hit brick from bottom");
                direction.y *= -1;
                transform.setTy(ob.minY() - r.y);
                hitSomething = true;
            }
        } else if (op.x < r.minX()) {
            // log().info("left from brick");
            if (Lines.linesIntersect(op.x, op.y, np.x, np.y, r.minX(),
                    r.minY(), r.minX(), r.maxY())) {
                // log().info("hit brick from left");
                direction.x *= -1;
                transform.setTx(r.x - ob.maxX());
                hitSomething = true;
            }
        } else if (op.y < r.minY()) {
            // log().info("top from brick");
            if (Lines.linesIntersect(op.x, op.y, np.x, np.y, r.minX(),
                    r.minY(), r.maxX(), r.minY())) {
                // log().info("hit brick from top");
                direction.y *= -1;
                transform.setTy(r.y - ob.maxY());
                hitSomething = true;
            }
        }
        if (hitSomething) {
            brick.removeHitpoint();
            if (brick.isBroken()) {
                board.removeBrick(brick);
            }
        }

        return hitSomething;
    }

}
