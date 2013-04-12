package com.gameforge.gamejam.pongout.core.play;

import playn.core.Color;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.PongoutSprite;

public class Ball extends PongoutSprite {
	private static final Dimension DIMENSIONS = new Dimension(20, 20);
	private static final Vector OFFSET = new Vector();
	private float speed;

	Ball() {
		super(DIMENSIONS, OFFSET);
		setDrawBoundary(true);
		setBoundaryColor(Color.rgb(0, 0, 255));
		translate(new Vector(100, 100));
	}
}
