package com.gameforge.gamejam.pongout.core.play;

import static playn.core.PlayN.log;
import playn.core.Color;
import pythagoras.f.AffineTransform;
import pythagoras.f.Dimension;
import pythagoras.f.Lines;
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
	AffineTransform transform;
	Board board;
	private PongoutSprite sprite;

	Ball(Board board, Vector direction) {
		this.board = board;
		sprite = new PongoutSprite(DIMENSION, OFFSET);
		addChild(sprite);

		speed = INITIAL_SPEED;
		this.direction = direction.normalize();

		setDrawBoundary(true);
		setBoundaryColor(Color.rgb(0, 0, 255));
	}

	public void update(float delta) {
		oldBoundingRectangle = getWorldBound().clone();

		Vector dist = direction.scale(speed);
		transform = new AffineTransform();
		transform.translate(dist.x, dist.y);

		BoundingRectangle ob = oldBoundingRectangle;
		BoundingRectangle nb = getWorldBound().translate(transform);

		Vector op = new Vector(ob.center().x, ob.center().y);
		Vector np = new Vector(nb.center().x, nb.center().y);

		// hit upper or lower bounds
		if (nb.minY() <= Board.OFFSET.y) {
			float newY = ob.minY() - Board.OFFSET.y;
			transform.setTy(newY);
			direction.y *= -1;
		}
		if (nb.maxY() >= Board.BOTTOM) {
			float newY = nb.maxY() - Board.BOTTOM;
			transform.setTy(newY);
			direction.y *= -1;
		}

		// player 1 paddle
		BoundingRectangle r = board.player1Paddle.getWorldBound();
		Vector ro = new Vector(r.maxX(), r.minY());
		Vector rd = new Vector(r.maxX(), r.maxY());

		if (Lines
				.linesIntersect(op.x, op.y, np.x, np.y, ro.x, ro.y, rd.x, rd.y)) {
			log().info("asdasd");
			float newX = ob.minX() - ro.x + 100;
			transform.setTx(newX);
			direction.x *= -1;
		}

		board.draw[0] = op.clone();
		board.draw[1] = np.clone();
		board.draw[2] = ro.clone();
		board.draw[3] = rd.clone();
		// board.draw[4] = intersection.clone();

		transform(transform);

		super.update(delta);
	}

	public Point center() {
		return getWorldBound().center();
	}
}
