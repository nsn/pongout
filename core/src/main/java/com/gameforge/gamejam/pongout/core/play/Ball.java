package com.gameforge.gamejam.pongout.core.play;

import playn.core.Color;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.PongoutSprite;

public class Ball extends PongoutSprite {
	private static final float INITIAL_SPEED = 5.0f; // pixels per ms
	private static final Dimension DIMENSIONS = new Dimension(20, 20);
	private static final Vector OFFSET = new Vector();
	private float speed; // pixels/ms
	private Vector direction;

	Ball(Vector direction) {
		super(DIMENSIONS, OFFSET);
		speed = INITIAL_SPEED;
		this.direction = direction.normalize();

		setDrawBoundary(true);
		setBoundaryColor(Color.rgb(0, 0, 255));
	}

	@Override
	public void update(float delta) {
		translate(direction.scale(speed));
		super.update(delta);
	}

}
