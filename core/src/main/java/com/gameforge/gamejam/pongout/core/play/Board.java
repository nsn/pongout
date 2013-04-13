package com.gameforge.gamejam.pongout.core.play;

import static playn.core.PlayN.log;

import java.util.ArrayList;

import playn.core.Color;
import pythagoras.f.AffineTransform;
import pythagoras.f.Dimension;
import pythagoras.f.Ray2;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;

import com.nightspawn.sg.BoundingRectangle;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Node;

public class Board extends GroupNode<Node> {
	public static final Vector OFFSET = new Vector(0.0f, 48.0f);
	public static final Dimension DIMENSION = new Dimension(1280, 730);
	private ArrayList<Ball> balls;
	Vector[] draw = { new Vector(), new Vector(), new Vector(), new Vector(),
			new Vector() };
	Paddle player1Paddle;
	Paddle player2Paddle;

	public Board(UserInput player1Input, UserInput player2Input) {
		setTranslation(OFFSET);
		balls = new ArrayList<Ball>();

		setDrawBoundary(true);
		setBoundaryColor(Color.blue(255));
		player1Paddle = new Paddle(player1Input, 0);
		player1Paddle.translate(new Vector(100, 100));
		addChild(player1Paddle);

		player2Paddle = new Paddle(player2Input, 1);
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
		// my bounds
		Rectangle r = new Rectangle(OFFSET.x, OFFSET.y, DIMENSION.width,
				DIMENSION.height);
		Vector tl = new Vector(OFFSET.x, OFFSET.y);
		Vector tr = new Vector(OFFSET.x + DIMENSION.width, OFFSET.y);
		Ray2 upper = new Ray2(tl, new Vector(1, 0));
		// move balls
		for (Ball b : balls) {
			AffineTransform t = b.trans;
			BoundingRectangle ob = b.getWorldBound();
			Vector op = new Vector(ob.x, ob.y);
			BoundingRectangle nb = b.newBoundingRectangle;
			Vector np = new Vector(nb.x, nb.y);

			Ray2 mov;
			Vector intersection;

			// test?
			// Vector o1 = new Vector(0, 100);
			// Vector d1 = new Vector(500, 100);
			// Vector o2 = new Vector(100, 0);
			// Vector d2 = new Vector(100, 200);
			// mov = new Ray2(o1, d1.subtract(o1).normalize());
			// intersection = new Vector();
			// if (mov.getIntersection(o2, d2, intersection)) {
			// draw[0] = o1.clone();
			// draw[1] = d1.clone();
			// draw[2] = o2.clone();
			// draw[3] = d2.clone();
			// draw[4] = intersection.clone();
			//
			// // log().debug("intersection " + intersection);
			// // float dist = intersection.distance(op);
			// // float len = np.distance(op);
			// // log().debug("aasd " + len + " " + dist);
			// }

			intersection = new Vector();
			// upper?
			if (upper.getIntersection(op, np, intersection)) {
				draw[0] = op.clone();
				draw[1] = np.clone();
				draw[2] = tl.clone();
				draw[3] = tr.clone();
				draw[4] = intersection.clone();

				float dist = intersection.distance(op);
				float len = np.distance(op);
				float ratio = 1 - (dist / len);

				log().debug("aasd " + len + " " + dist + " " + ratio);
			}

			// hit upper or lower bounds
			if (nb.minY() <= r.minY()) {
				float newY = ob.minY() - r.minY();
				t.setTy(newY);
				b.direction.y *= -1;
			}

			if (nb.maxY() >= r.maxY()) {
				float newY = r.maxY() - ob.maxY();
				// t.setTy(newY);
				b.direction.y *= -1;
			}

			// Vector intersection = intersect(op, np, new Vector(0.0f,
			// OFFSET.y + DIMENSION.height), new Vector(1.0f, OFFSET.y
			// + DIMENSION.height));
			// draw[0] = op.clone();
			// draw[1] = np.clone();
			// draw[2] = intersection.clone();
			// float dist = intersection.distance(op);
			// float length = np.subtract(op).length();
			// float rest = length - dist;
			// log().info(
			// "asdasd " + dist + "  " + length + "  " + rest
			// + " --> " + intersection + " / " + np + " <-> "
			// + op + " dafuq? " + np.subtract(op));
			// /b.direction.y *= -1;
			// }
			// hit paddles?
			b.transform(t);
		}

	}
}
