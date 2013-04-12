package com.gameforge.gamejam.pongout.core.play;

import java.util.ArrayList;

import pythagoras.f.Vector;

import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Node;

public class Board extends GroupNode<Node> {
	private ArrayList<Ball> balls;

	public Board() {
		balls = new ArrayList<Ball>();
	}

	public void spawnBall() {
		// direction
		Vector dir = new Vector(1, -1);

		// position
		Vector pos = new Vector(200, 200);

		Ball b = new Ball(dir);
		b.setTranslation(pos);

		balls.add(b);
		addChild(b);
	}

}
