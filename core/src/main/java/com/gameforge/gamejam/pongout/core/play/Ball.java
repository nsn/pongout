package com.gameforge.gamejam.pongout.core.play;

import playn.core.Color;
import pythagoras.f.AffineTransform;
import pythagoras.f.Dimension;
import pythagoras.f.Point;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.PongoutSprite;
import com.nightspawn.sg.BoundingRectangle;

public class Ball extends GameObject {
	private static final float INITIAL_SPEED = 12f; // pixels per msec
	private static final Dimension DIMENSION = new Dimension(20, 20);
	private static final Vector OFFSET = new Vector(0, 420);
	private float speed; // pixels/ms
	Vector direction;
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
		super.update(delta);

		Vector dist = direction.scale(speed);

		AffineTransform trans = new AffineTransform();
		trans.translate(dist.x, dist.y);

		BoundingRectangle ob = oldBoundingRectangle;
		BoundingRectangle nb = getWorldBound().translate(trans);

		// hit upper or lower bounds
		if (nb.minY() <= Board.OFFSET.y) {
			float newY = ob.minY() - Board.OFFSET.y;
			trans.setTy(newY);
			direction.y *= -1;
		}
		if (nb.maxY() >= Board.BOTTOM) {
			float newY = nb.maxY() - Board.BOTTOM;
			trans.setTy(newY);
			direction.y *= -1;
		}

		transform(trans);
	}

	public Point center() {
		return getWorldBound().center();
	}
}
