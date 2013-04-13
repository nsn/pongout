package com.gameforge.gamejam.pongout.core.play;

import static playn.core.PlayN.log;
import pythagoras.f.AffineTransform;
import pythagoras.f.Line;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;

import com.nightspawn.sg.BoundingRectangle;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Node;

public class GameObject extends GroupNode<Node> {
	protected Vector velocity = new Vector(); // pixels/ms
	BoundingRectangle newBoundingRectangle;
	AffineTransform trans;

	public void collidesWith(Rectangle r) {
		BoundingRectangle nb = newBoundingRectangle;

		Point ul = r.location();
		Point ur = new Point(ul.x + r.width, ul.y);
		Point ll = new Point(ul.x, ul.y + r.height);
		Point lr = new Point(ur.x, ll.y);
		Line left = new Line(ul, ll);
		Line right = new Line(ur, lr);
		Line top = new Line(ul, ur);
		Line bottom = new Line(ll, lr);

		if (nb.intersectsLine(left))
			log().info("left");
		if (nb.intersectsLine(right))
			log().info("right");
		if (nb.intersectsLine(top))
			log().info("top");
		if (nb.intersectsLine(bottom))
			log().info("bottom");

	}

	@Override
	public void update(float deltaMs) {
		super.update(deltaMs);
		float deltaSec = deltaMs / 1000;

		trans = new AffineTransform();
		Vector distance = velocity.scale(deltaSec);
		trans.translate(distance.x, distance.y);

		BoundingRectangle ob = getWorldBound();
		BoundingRectangle nb = ob.translate(trans);

		newBoundingRectangle = nb;
	}

}
