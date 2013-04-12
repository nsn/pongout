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

	public Board() {
		setTranslation(OFFSET);
		balls = new ArrayList<Ball>();

		setDrawBoundary(true);
		setBoundaryColor(Color.blue(255));
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

}
