package com.gameforge.gamejam.pongout.core.play;

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
	private ArrayList<Ball> balls;
	private ArrayList<Ball> ballsToRemove;

	Vector[] draw = { new Vector(), new Vector(), new Vector(), new Vector(),
			new Vector() };
	Paddle player1Paddle;
	Paddle player2Paddle;
	Scores scores;
	BrickLayout brickLayout;
	boolean gameOver;

	public Board(UserInput player1Input, UserInput player2Input) {
		setTranslation(OFFSET);
		balls = new ArrayList<Ball>();
		ballsToRemove = new ArrayList<Ball>();

		setDrawBoundary(true);
		setBoundaryColor(Color.blue(255));
		player1Paddle = new Paddle(player1Input, 0);
		player1Paddle.translate(new Vector(100, 100));
		addChild(player1Paddle);

		player2Paddle = new Paddle(player2Input, 1);
		player2Paddle.translate(new Vector(1150, 100));
		addChild(player2Paddle);

		scores = new Scores();
		addChild(scores);

		brickLayout = new BrickLayout();
		addChild(brickLayout);
	}

	public void spawnBall() {
		Random rand = new Random();
		// direction
		Vector dir = new Vector(-0.1f, 0.0f);
		// position
		Vector pos = new Vector(320, 150);

		Ball b = new Ball(this, dir);
		b.setTranslation(pos);
		balls.add(b);
		addChild(b);
	}

	public void removeBall(Ball b) {
		ballsToRemove.add(b);
	}

	@Override
	public void update(float deltams) {
		// spawn ball if there are no balls left
		if (balls.isEmpty()) {
			spawnBall();
		}
        for (Ball ball : ballsToRemove) {
            balls.remove(ball);
            getChildren().remove(ball);
        }
        ballsToRemove.clear();
		super.update(deltams);
	}

}
