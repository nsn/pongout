package com.gameforge.gamejam.pongout.core.play;

import static com.gameforge.gamejam.pongout.core.play.Player.PLAYER1;
import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.Random;

import playn.core.Color;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;

import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Node;

public class Board extends GroupNode<Node> {
    public static final Vector OFFSET = new Vector(0.0f, 48.0f);
    public static final Dimension DIMENSION = new Dimension(1280, 730);
    public static final float BOTTOM = OFFSET.y + DIMENSION.height;
    public static final float LEFT = OFFSET.x;
    public static final float RIGHT = OFFSET.x + DIMENSION.width;
    public static final int POWERUP_DROPCHANCE = 125;
    private ArrayList<Ball> balls;
    private ArrayList<Ball> ballsToRemove;
    private ArrayList<Ball> ballsToSpawn;
    private ArrayList<Brick> bricksToRemove;
    private ArrayList<PowerUp> powerUpsToRemove;
    ArrayList<PowerUp> powerUps;

    Vector[] draw = { new Vector(), new Vector(), new Vector(), new Vector(),
            new Vector(), new Vector(), new Vector() };
    Paddle player1Paddle;
    Paddle player2Paddle;
    Scores scores;
    BrickLayout brickLayout;
    boolean gameOver;

    public Board(UserInput player1Input, UserInput player2Input) {
        setTranslation(OFFSET);
        balls = new ArrayList<Ball>();
        ballsToRemove = new ArrayList<Ball>();
        ballsToSpawn = new ArrayList<Ball>();
        bricksToRemove = new ArrayList<Brick>();
        powerUpsToRemove = new ArrayList<PowerUp>();
        powerUps = new ArrayList<PowerUp>();

        setDrawBoundary(false);
        setBoundaryColor(Color.blue(255));
        player1Paddle = new Paddle(player1Input, 0, Player.PLAYER1);
        player1Paddle.translate(new Vector(100, 100));
        addChild(player1Paddle);

        player2Paddle = new Paddle(player2Input, 1, Player.PLAYER2);
        player2Paddle.translate(new Vector(1150, 100));
        addChild(player2Paddle);

        scores = new Scores();
        addChild(scores);

        brickLayout = new BrickLayout();
        addChild(brickLayout);

    }

    public void spawnBall(Vector position) {
        // direction
        Random rand = new Random();
        Vector dir = new Vector(-0.1f, rand.nextFloat() - 0.5f);
        Ball b = new Ball(this, dir);

        b.setTranslation(position);
        balls.add(b);
        addChild(b);

    }

    public void spawnBall() {
        Vector pos = new Vector(800, 100);
        spawnBall(pos);
    }

    public void removeBall(Ball b) {
        ballsToRemove.add(b);
    }

    public void removeBrick(Brick b) {
        bricksToRemove.add(b);
    }

    public void collectPowerup(PowerUp p, Ball b) {
        log().info("collection powerup");
        powerUpsToRemove.add(p);
        Paddle lastActivePaddle = null;
        if (b.lastBounce != Player.NONE) {
            lastActivePaddle = b.lastBounce == PLAYER1 ? player1Paddle
                    : player2Paddle;
        }
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
        }

    }

    @Override
    public void update(float deltams) {
        // spawn ball if there are no balls left
        if (balls.isEmpty()) {
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
            if (roll < POWERUP_DROPCHANCE) {
                PowerUp pu = RandomPowerUpGenerator.generate(brick);
                powerUps.add(pu);
                addChild(pu);
            }
        }
        bricksToRemove.clear();
        for (PowerUp powerUp : powerUpsToRemove) {
            powerUps.remove(powerUp);
            getChildren().remove(powerUp);
        }
        super.update(deltams);
    }

}
