package com.gameforge.gamejam.pongout.core.play;

import java.util.ArrayList;

import playn.core.Color;
import pythagoras.f.AffineTransform;
import pythagoras.f.Dimension;
import pythagoras.f.MathUtil;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;

import com.nightspawn.sg.BoundingRectangle;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Node;

public class Board extends GroupNode<Node> {
	public static final Vector OFFSET = new Vector(0.0f, 48.0f);
	public static final Dimension DIMENSION = new Dimension(1280, 730);
	private ArrayList<Ball> balls;
	Vector[] draw = { new Vector(), new Vector(), new Vector() };
	Paddle player1Paddle;
	Paddle player2Paddle;
    private Scores scores;

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
        
        scores = new Scores();
        addChild(scores);
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

	private Vector intersect(Vector p1, Vector p2, Vector p3, Vector p4) {
		float d = ((p1.x - p2.x) * (p3.y - p4.y))
				- ((p1.y - p2.y) * (p3.x - p4.x));
		if (MathUtil.epsilonEquals(d, 0.0f)) {
			// If parallel, defaults to the average location of the
			return new Vector(p1.x + p3.x, p1.y + p3.y).scale(.5f);
		}
		float a = (p1.x * p2.y) - (p1.y * p2.x);
		float b = (p3.x * p4.y) - (p3.y * p4.x);
		return new Vector(((a * (p3.x - p4.x)) - ((p1.x - p2.x) * b)) / d,
				((a * (p3.y - p4.y)) - ((p1.y - p2.y) * b)) / d);
	}

	@Override
	public void update(float deltams) {
		super.update(deltams);
		// my bounds
		Rectangle r = new Rectangle(OFFSET.x, OFFSET.y, DIMENSION.width,
				DIMENSION.height);
		// move balls
		for (Ball b : balls) {
			AffineTransform t = b.trans;
			BoundingRectangle ob = b.getWorldBound();
			Vector op = new Vector(ob.x, ob.y);
			BoundingRectangle nb = b.newBoundingRectangle;
			Vector np = new Vector(nb.x, nb.y);
			// hit upper or lower bounds
			if (nb.minY() <= r.minY() || nb.maxY() >= r.maxY()) {
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
				b.direction.y *= -1;
			}
			b.transform(t);
		}

	}
}
