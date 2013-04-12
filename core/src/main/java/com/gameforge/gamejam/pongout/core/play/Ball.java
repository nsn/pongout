package com.gameforge.gamejam.pongout.core.play;

import static playn.core.PlayN.log;
import playn.core.Color;
import pythagoras.f.AffineTransform;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.PongoutSprite;
import com.nightspawn.sg.BoundingRectangle;

public class Ball extends PongoutSprite {
	private static final float INITIAL_SPEED = 5.0f; // pixels per ms
	private static final Dimension DIMENSION = new Dimension(20, 20);
	private static final Vector OFFSET = new Vector();
	private float speed; // pixels/ms
	private Vector direction;

	Ball(Vector direction) {
		super(DIMENSION, OFFSET);
		speed = INITIAL_SPEED;
		this.direction = direction.normalize();

		setDrawBoundary(true);
		setBoundaryColor(Color.rgb(0, 0, 255));
	}

	@Override
	public void update(float delta) {
		AffineTransform trans = new AffineTransform();
		// movement
		Vector movement = direction.scale(speed);
		trans.translate(movement.x, movement.y);

		BoundingRectangle ob = getWorldBound();
		BoundingRectangle nb = ob.translate(trans);

		log().info("foo " + ob.minY() + " " + nb.minY() + " " + Board.OFFSET.y);

		if (nb.minY() <= Board.OFFSET.y) {
			// collide!
			direction.y *= -1;
		}
		if (nb.maxY() >= (Board.OFFSET.y + Board.DIMENSION.height)) {
			direction.y *= -1;
		}

		transform(trans);

		super.update(delta);
	}
}
