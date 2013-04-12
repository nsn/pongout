package com.gameforge.gamejam.pongout.core.play;

import playn.core.Color;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.PongoutSprite;

public class Ball extends GameObject {
	private static final float INITIAL_SPEED = 100f; // pixels per sec
	private static final Dimension DIMENSION = new Dimension(20, 20);
	private static final Vector OFFSET = new Vector();
	private float speed; // pixels/ms
	private Vector direction;
	private PongoutSprite sprite;

	Ball(Vector direction) {
		sprite = new PongoutSprite(DIMENSION, OFFSET);
		addChild(sprite);

		speed = INITIAL_SPEED;
		this.direction = direction.normalize();

		setDrawBoundary(true);
		setBoundaryColor(Color.rgb(0, 0, 255));
	}

	public void update(float delta) {
		velocity = direction.scale(speed);
		super.update(delta);
	}
}
