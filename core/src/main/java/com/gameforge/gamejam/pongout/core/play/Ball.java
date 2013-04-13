package com.gameforge.gamejam.pongout.core.play;

import static playn.core.PlayN.log;

import java.util.Iterator;

import playn.core.Color;
import pythagoras.f.AffineTransform;
import pythagoras.f.Dimension;
import pythagoras.f.Lines;
import pythagoras.f.MathUtil;
import pythagoras.f.Point;
import pythagoras.f.Ray2;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.PongoutSprite;
import com.nightspawn.sg.BoundingRectangle;
import com.nightspawn.sg.Spatial;

public class Ball extends GameObject {
    private static final float INITIAL_SPEED = 7f; // pixels per msec
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
            board.collectPowerup(powerup, this);
        }
        return hitSomething;
    }

    private boolean bounceLine(Vector s, Vector e, float curve) {
        if (Lines.linesIntersect(op.x, op.y, np.x, np.y, s.x, s.y, e.x, e.y)) {
            log().debug("intersect");
            // intersection
            Ray2 movement = new Ray2(op, np.subtract(op).normalize());
            Vector intersection = new Vector();
            if (!movement.getIntersection(s, e, intersection))
                return false;

            // direction
            Vector normal = new Vector((s.y - e.y) * -1, s.x - e.x).normalize();
            float dot = normal.dot(direction);
            Vector diff = normal.scale(2 * dot);

            // flat panel
            direction = direction.subtract(diff).normalize();
            // curve
            float ratio = s.distance(intersection) / s.distance(e) * 2.0f - 1;
            direction.y += curve * ratio;

            transform.setTx(op.x - intersection.x);
            transform.setTy(op.y - intersection.y);

            board.draw[4] = intersection.clone();

            board.draw[2] = intersection.clone();
            board.draw[3] = intersection.add(direction.scale(speed * 2));
            board.draw[5] = intersection.clone();
            board.draw[6] = intersection.add(normal.scale(speed));

            return true;
        }
        return false;
    }

    private void bouncePaddle(Paddle paddle, boolean left) {
        Rectangle r = paddle.getBounceRectangle();
        float xOffset = left ? r.width : 0.0f;
        Vector ro = new Vector(r.minX() + xOffset, r.minY());
        Vector rd = new Vector(r.minX() + xOffset, r.maxY());
        float ed = left ? -1 : 1;
        Vector be = new Vector(rd.x + (Paddle.PADDLE_WIDTH * .5f * ed), rd.y
                + (Paddle.PADDLE_WIDTH * .5f));
        Vector te = new Vector(ro.x + (Paddle.PADDLE_WIDTH * .5f * ed), ro.y
                - (Paddle.PADDLE_WIDTH * .5f));
        boolean bounced = false;
        bounced = bounced || bounceLine(ro, rd, Paddle.CURVE);
        bounced = bounced || bounceLine(te, ro, Paddle.CURVE);
        bounced = bounced || bounceLine(rd, be, Paddle.CURVE);
        log().info("bouncePaddle - last paddle is " + paddle.getName());
        if(bounced) {
            lastBounce = paddle.player;                
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
            if (bouncePowerup(powerUp)) {
                break;
            }
        }

        transform(transform);
        if (MathUtil.epsilonEquals(direction.x, 0.0f)) {
            direction.x += 0.02f;
        }
        direction.normalizeLocal();
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
