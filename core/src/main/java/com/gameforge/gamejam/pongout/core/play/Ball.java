package com.gameforge.gamejam.pongout.core.play;

import static playn.core.PlayN.log;

import java.util.Iterator;

import playn.core.Color;
import pythagoras.f.AffineTransform;
import pythagoras.f.Dimension;
import pythagoras.f.Lines;
import pythagoras.f.MathUtil;
import pythagoras.f.Point;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.PongoutSprite;
import com.nightspawn.sg.BoundingRectangle;
import com.nightspawn.sg.Spatial;

public class Ball extends GameObject {
    private static final float INITIAL_SPEED = 12f; // pixels per msec
    private static final Dimension DIMENSION = new Dimension(20, 20);
    private static final Vector OFFSET = new Vector(0, 420);
    public static final float RADIUS = DIMENSION.width / 2;
    float speed; // pixels/ms
    Vector direction;
    AffineTransform transform;
    BoundingRectangle ob;
    BoundingRectangle nb;
    Vector op;
    Vector np;
    Board board;
    Player lastBounce = Player.NONE;
    private PongoutSprite sprite;
    boolean isBomb = false;
    boolean isFast = false;

    Ball(Board board, Vector direction) {
        this.board = board;
        sprite = new PongoutSprite(DIMENSION, OFFSET);
        addChild(sprite);

        speed = INITIAL_SPEED;
        this.direction = direction.normalize();

        setDrawBoundary(false);
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
                // direction.x *= -1;
                // transform.setTx(ob.minX() - r.x);
                hitSomething = true;
            }
        } else if (op.y > r.maxY()) {
            // log().info("bottom from brick");
            if (Lines.linesIntersect(op.x, op.y, np.x, np.y, r.minX(),
                    r.maxY(), r.maxX(), r.maxY())) {
                // log().info("hit brick from bottom");
                // direction.y *= -1;
                // transform.setTy(ob.minY() - r.y);
                hitSomething = true;
            }
        } else if (op.x < r.minX()) {
            // log().info("left from brick");
            if (Lines.linesIntersect(op.x, op.y, np.x, np.y, r.minX(),
                    r.minY(), r.minX(), r.maxY())) {
                // log().info("hit brick from left");
                // direction.x *= -1;
                // transform.setTx(r.x - ob.maxX());
                hitSomething = true;
            }
        } else if (op.y < r.minY()) {
            // log().info("top from brick");
            if (Lines.linesIntersect(op.x, op.y, np.x, np.y, r.minX(),
                    r.minY(), r.maxX(), r.minY())) {
                // log().info("hit brick from top");
                // direction.y *= -1;
                // transform.setTy(r.y - ob.maxY());
                hitSomething = true;
            }
        }
        if (hitSomething) {
            board.collectPowerup(powerup, this);
        }
        return hitSomething;
    }

    private Vector closestPointOnSeq(Vector segA, Vector segB) {
        Vector segV = segB.subtract(segA);
        Vector ptV = np.subtract(segA);
        Vector segVUnit = segV.normalize();
        float proj = ptV.dot(segVUnit);
        if (proj <= 0.0f)
            return segA.clone();
        if (proj > segV.length())
            return segB.clone();
        Vector ProjV = segVUnit.scale(proj);
        return ProjV.add(segA);
    }

    private boolean bounceLine(Vector s, Vector e, float curve) {
        Vector closest = closestPointOnSeq(s, e);
        Vector distV = np.subtract(closest);
        float dist = distV.length();
        if (dist < RADIUS) {
            Vector distVUnit = distV.normalize();
            Vector offset = distVUnit.scale(RADIUS - distV.length());

            Vector translation = op.subtract(np.subtract(offset));
            translation = direction.normalize().scale(-dist);
            transform.setTx(translation.x);
            transform.setTy(translation.y);

            // direction
            Vector normal = new Vector((s.y - e.y) * -1, s.x - e.x).normalize();
            float dot = normal.dot(direction);
            Vector diff = normal.scale(2 * dot);

            // flat panel
            direction = direction.subtract(diff).normalize();
            // curve
            float ratio = s.distance(closest) / s.distance(e) * 2.0f - 1;
            direction.y += curve * ratio;
            direction.normalizeLocal();

            board.draw[5] = closest.clone();
            board.draw[6] = np.subtract(offset);

            nb = getWorldBound().translate(transform);
            np = new Vector(nb.center().x, nb.center().y);
            return true;
        }
        return false;
    }

    private boolean bouncePaddle(Paddle paddle, boolean left) {
        Vector[] b = paddle.getBound();
        boolean bounced = false;
        bounced = bounced
                || bounceLine(b[Paddle.FRONT_TOP], b[Paddle.FRONT_BOTTOM],
                        Paddle.CURVE);
        bounced = bounced
                || bounceLine(b[Paddle.FRONT_TOP], b[Paddle.BACK_TOP],
                        Paddle.CURVE);
        bounced = bounced
                || bounceLine(b[Paddle.FRONT_BOTTOM], b[Paddle.BACK_BOTTOM],
                        Paddle.CURVE);
        bounced = bounced
                || bounceLine(b[Paddle.BACK_TOP], b[Paddle.BACK_BOTTOM],
                        Paddle.CURVE);

        if (bounced) {
            lastBounce = paddle.player;
            SoundStore.getInstance().getSound("blip").play();
        }

        if (nb.intersects(paddle.getBounceRectangle())) {
            if (left) {
                transform.setTx(ob.minX() - b[Paddle.FRONT_TOP].x);
            } else {
                transform.setTx(b[Paddle.FRONT_TOP].x - ob.maxX());
            }
        }

        return bounced;
    }

    public void update(float delta) {
        super.update(delta);
        oldBoundingRectangle = getWorldBound().clone();

        Vector dist = direction.scale(speed);
        transform = new AffineTransform();
        transform.translate(dist.x, dist.y);

        ob = oldBoundingRectangle;
        op = new Vector(ob.center().x, ob.center().y);

        nb = getWorldBound().translate(transform);
        np = new Vector(nb.center().x, nb.center().y);

        Vector diff = direction.scale(ob.width * .5f);

        board.draw[0] = op.clone();
        board.draw[1] = np.clone();
        board.draw[2] = op.add(diff);
        board.draw[3] = np.subtract(diff);

        // log().info("asdasd " + op + " <-> " + np);

        // leaves play area
        if (np.x < Board.LEFT) {
            log().info("LEFT " + np.x);
            board.removeBall(this);
            board.scores.removePointForPlayer1();
            SoundStore.getInstance().getSound("damage").play();
            return;
        }
        if (np.x > Board.RIGHT) {
            board.removeBall(this);
            board.scores.removePointForPlayer2();
            SoundStore.getInstance().getSound("damage").play();
            return;
        }

        bouncePaddle(board.player1Paddle, true);
        bouncePaddle(board.player2Paddle, false);

        // bricks
        for (Iterator<Spatial> it = board.brickLayout.getBricks().iterator(); it
                .hasNext();) {
            Brick brick = (Brick) it.next();
            if (bounceBrick(brick)) {
                break;
            }
        }

        // walls
        for (Iterator<Spatial> it = board.brickWallPlayer1.getChildren()
                .iterator(); it.hasNext();) {
            Brick brick = (Brick) it.next();
            if (bounceBrick(brick)) {
                break;
            }
        }
        for (Iterator<Spatial> it = board.brickWallPlayer2.getChildren()
                .iterator(); it.hasNext();) {
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

        nb = getWorldBound().translate(transform);
        np = new Vector(nb.center().x, nb.center().y);

        // hit upper or lower bounds
        if (nb.minY() <= Board.OFFSET.y) {
            float newY = ob.minY() - Board.OFFSET.y + RADIUS;
            transform.setTy(newY);
            direction.y *= -1;
        }
        if (nb.maxY() >= Board.BOTTOM) {
            float newY = nb.maxY() - Board.BOTTOM - RADIUS;
            transform.setTy(newY);
            direction.y *= -1;
        }

        transform(transform);
        if (MathUtil.epsilonEquals(direction.x, 0.0f)) {
            direction.x += 0.02f;
        }
        direction.normalizeLocal();
    }

    public Point center() {
        return getWorldBound().center();
    }

    private boolean bounceBrick(Brick brick) {

        Vector oldDirection = direction;
        AffineTransform oldTransform = transform;

        BoundingRectangle r = brick.sprite.getWorldBound();
        boolean hitSomething = false;
        if (op.x > r.maxX()) {
            hitSomething = hitSomething
                    || bounceLine(new Vector(r.maxX(), r.minY()),
                            new Vector(r.maxX(), r.maxY()), 0.0f);
        } else if (op.y > r.maxY()) {
            hitSomething = hitSomething
                    || bounceLine(new Vector(r.minX(), r.maxY()),
                            new Vector(r.maxX(), r.maxY()), 0.0f);
        } else if (op.x < r.minX()) {
            hitSomething = hitSomething
                    || bounceLine(new Vector(r.minX(), r.minY()),
                            new Vector(r.minX(), r.maxY()), 0.0f);
        } else if (op.y < r.minY()) {
            hitSomething = hitSomething
                    || bounceLine(new Vector(r.minX(), r.minY()),
                            new Vector(r.maxX(), r.minY()), 0.0f);
        }
        if (hitSomething) {
            if (isBomb) {
                direction = oldDirection;
                transform = oldTransform;
            }
            brick.removeHitpoint();
            if (brick.isBroken()) {
                board.removeBrick(brick);
            }
        }

        return hitSomething;
    }

    public void setFast() {
        isFast = true;
        sprite.setFrame(2);
    }

    public void setBomb() {
        isBomb = true;
        sprite.setFrame(1);
    }

}
