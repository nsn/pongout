package com.gameforge.gamejam.pongout.core.play;

import java.util.ArrayList;

import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Node;

public class Board extends GroupNode<Node> {
	private ArrayList<Ball> balls;

	public Board() {
		balls = new ArrayList<Ball>();
	}

	public void spawnBall() {
		Ball b = new Ball();
		balls.add(b);
		addChild(b);
	}

}
