package com.gameforge.gamejam.pongout.core.play;

import static com.gameforge.gamejam.pongout.core.play.Player.PLAYER1;
import static playn.core.PlayN.log;
import static playn.core.PlayN.random;

import java.util.ArrayList;
import java.util.Random;

import playn.core.Color;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;

import com.nightspawn.sg.Area;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Node;

public class Board extends GroupNode<Node> {
    public static final Vector OFFSET = new Vector(20.0f, 60.0f);
    public static final Dimension DIMENSION = new Dimension(1240, 660);
    public static final float TOP = OFFSET.y;
    public static final float BOTTOM = OFFSET.y + DIMENSION.height;
    public static final float LEFT = OFFSET.x;
    public static final float RIGHT = OFFSET.x + DIMENSION.width;
    public static final int POWERUP_DROPCHANCE = 25;
    private ArrayList<Ball> balls;
    private ArrayList<Ball> ballsToRemove;
    private ArrayList<Ball> ballsToSpawn;
    private ArrayList<Brick> bricksToRemove;
    private ArrayList<Brick> wallBricksToRemove;
    private ArrayList<PowerUp> powerUpsToRemove;
    ArrayList<PowerUp> powerUps;

    Vector[] draw = { new Vector(), new Vector(), new Vector(), new Vector(),
            new Vector(), new Vector(), new Vector() };
    Paddle player1Paddle;
    Paddle player2Paddle;
    Scores scores;
    BrickLayout brickLayout;
    BrickWall brickWallPlayer1;
    BrickWall brickWallPlayer2;

    boolean gameOver;
    private boolean increaseBallSpeed = false;

    public Board(UserInput player1Input, UserInput player2Input) {
        setTranslation(OFFSET);
        balls = new ArrayList<Ball>();
        ballsToRemove = new ArrayList<Ball>();
        ballsToSpawn = new ArrayList<Ball>();
        bricksToRemove = new ArrayList<Brick>();
        wallBricksToRemove = new ArrayList<Brick>();
        powerUpsToRemove = new ArrayList<PowerUp>();
        powerUps = new ArrayList<PowerUp>();

        setDrawBoundary(false);
        setBoundaryColor(Color.blue(255));
        player1Paddle = new Paddle(player1Input, 0, Player.PLAYER1);
        player1Paddle.translate(new Vector(60, 100));
        player1Paddle.setName("player1");
        addChild(player1Paddle);

        player2Paddle = new Paddle(player2Input, 1, Player.PLAYER2);
        player2Paddle.translate(new Vector(1150, 100));
        player2Paddle.setName("player2");
        addChild(player2Paddle);

        brickLayout = new BrickLayout();
        addChild(brickLayout);

        brickWallPlayer1 = new BrickWall(0);
        addChild(brickWallPlayer1);
        brickWallPlayer2 = new BrickWall(1210);
        addChild(brickWallPlayer2);

        scores = new Scores();
        addChild(scores);

        Area a = new Area(DIMENSION);
        addChild(a);

        spawnBall(new Vector(player1Paddle.getLocalTransform().tx + 30,
                player1Paddle.getLocalTransform().ty));
        spawnBall(new Vector(player2Paddle.getLocalTransform().tx - 30,
                player2Paddle.getLocalTransform().ty));

        setBoundaryColor(Color.rgb(255, 0, 255));
        setDrawBoundary(false);

    }

    public void spawnBall(Vector position) {
        log().info("spawn ball gets called with pos " + position);
        // direction
        Vector dir = new Vector(-0.1f, random() - 0.5f);
        spawnBall(position, dir);
    }

    public void spawnBall(Vector position, Vector dir) {
        Ball b = new Ball(this, dir);

        b.setTranslation(position);
        balls.add(b);
        addChild(b);

    }

    public void spawnBall() {
        Vector pos = new Vector(800, 100);
        spawnBall(pos, new Vector(1, 0));
    }

    public void removeBall(Ball b) {
        ballsToRemove.add(b);
    }

    public void removeBrick(Brick b) {
        if (b.isWallBrick) {
            wallBricksToRemove.add(b);
        } else {
            bricksToRemove.add(b);
        }
    }

    public void collectPowerup(PowerUp p, Ball b) {
        log().info("collection powerup");
        powerUpsToRemove.add(p);
        Paddle lastActivePaddle = null;
        if (b.lastBounce != Player.NONE) {
            lastActivePaddle = b.lastBounce.equals(PLAYER1) ? player1Paddle
                    : player2Paddle;
        }
        log().info("last bounce is " + b.lastBounce.name());
        switch (p.getType()) {
        case ENLARGE:
            if (lastActivePaddle != null) {
                lastActivePaddle.increaseSize();
                lastActivePaddle.setCurrentPowerup(PowerUp.TYPE.ENLARGE);
            }
            break;
        case MULTIBALL:
            ballsToSpawn.add(b);
            if (lastActivePaddle != null) {
                lastActivePaddle.setCurrentPowerup(PowerUp.TYPE.MULTIBALL);
            }
            break;
        case SPEED:
            if (lastActivePaddle != null) {
                lastActivePaddle.setSpeed(1.5f);
                lastActivePaddle.setCurrentPowerup(PowerUp.TYPE.SPEED);
            }
            break;
        case CONTROLS:
            if (lastActivePaddle != null) {
                lastActivePaddle.getUserInput().switchKeys();
                lastActivePaddle.setCurrentPowerup(PowerUp.TYPE.CONTROLS);
            }
            break;
        case BALLSPEED:
            if (lastActivePaddle != null) {
                lastActivePaddle.setCurrentPowerup(PowerUp.TYPE.BALLSPEED);
            }
            b.setFast();
            increaseBallSpeed = true;
            break;
        case BOMB:
            if (lastActivePaddle != null) {
                lastActivePaddle.setCurrentPowerup(PowerUp.TYPE.BOMB);
            }
            b.setBomb();
            break;
        case REPAIR:
            if (lastActivePaddle != null) {
                lastActivePaddle.setCurrentPowerup(PowerUp.TYPE.REPAIR);
                if (lastActivePaddle == player1Paddle) {
                    brickWallPlayer1.repair();
                } else {
                    brickWallPlayer2.repair();
                }
            }
        }

    }

    @Override
    public void update(float deltams) {
        // spawn ball if there are no balls left
        if (balls.size() < 2) {
            spawnBall();
        }
        for (Ball ball : ballsToSpawn) {
            spawnBall(ball.getWorldPosition());
            spawnBall(ball.getWorldPosition());
        }
        ballsToSpawn.clear();
        for (Ball ball : ballsToRemove) {
            balls.remove(ball);
            getChildren().remove(ball);
        }
        ballsToRemove.clear();
        for (Brick brick : bricksToRemove) {
            log().info("removing brick...");
            brickLayout.getChildren().remove(brick);
            int roll = new Random().nextInt(100);
            if (roll < POWERUP_DROPCHANCE && !brick.isWallBrick) {
                PowerUp pu = RandomPowerUpGenerator.generate(brick);
                powerUps.add(pu);
                addChild(pu);
            }
        }
        bricksToRemove.clear();
        for (Brick brick : wallBricksToRemove) {
            brickWallPlayer1.getChildren().remove(brick);
            brickWallPlayer2.getChildren().remove(brick);
        }
        wallBricksToRemove.clear();
        if (brickLayout.getChildren().isEmpty()) {
            brickLayout.respawn();
        }
        if (increaseBallSpeed) {
            for (Ball ball : balls) {
                ball.speed *= 1.5;
            }
            increaseBallSpeed = false;
        }
        for (PowerUp powerUp : powerUpsToRemove) {
            powerUps.remove(powerUp);
            getChildren().remove(powerUp);
        }
        super.update(deltams);
    }

}
