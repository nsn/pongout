package com.gameforge.gamejam.pongout.core.play;

import java.util.ArrayList;

import playn.core.Color;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;

import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Node;

public class Board extends GroupNode<Node> {
	public static final Vector OFFSET = new Vector(0.0f, 48.0f);
	public static final Dimension DIMENSION = new Dimension(1280, 730);
	private ArrayList<Ball> balls;
    Paddle player1Paddle;
    Paddle player2Paddle;

	public Board(UserInput player1Input, UserInput player2Input) {
		setTranslation(OFFSET);
		balls = new ArrayList<Ball>();

		setDrawBoundary(true);
		setBoundaryColor(Color.blue(255));
        player1Paddle = new Paddle(player1Input,0);
        player1Paddle.translate(new Vector(100, 100));
		addChild(player1Paddle);
        
        player2Paddle = new Paddle(player2Input,1);
        player2Paddle.translate(new Vector(1150, 100));
        addChild(player2Paddle);
	}

	public void spawnBall() {
		// direction
		Vector dir = new Vector(0.1f, -1);

		// position
		Vector pos = new Vector(200, 200);

		Ball b = new Ball(dir);
		b.setTranslation(pos);

		balls.add(b);
		addChild(b);
	}

	@Override
	public void update(float deltams) {
		super.update(deltams);
		// move balls
		for (Ball b : balls) {
			b.transform(b.trans);
		}
	}

}
