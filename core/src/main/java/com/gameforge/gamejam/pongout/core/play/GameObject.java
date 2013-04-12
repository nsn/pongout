package com.gameforge.gamejam.pongout.core.play;

import pythagoras.f.AffineTransform;
import pythagoras.f.Vector;

import com.nightspawn.sg.BoundingRectangle;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Node;

public class GameObject extends GroupNode<Node> {
	protected Vector velocity = new Vector(); // pixels/ms
	BoundingRectangle newBoundingRectangle;
	AffineTransform trans;

	@Override
	public void update(float deltaMs) {
		super.update(deltaMs);
		float deltaSec = deltaMs / 1000;

		trans = new AffineTransform();
		Vector distance = velocity.scale(deltaSec);
		trans.translate(distance.x, distance.y);

		BoundingRectangle ob = getWorldBound();
		BoundingRectangle nb = ob.transform(trans);

		newBoundingRectangle = nb;
	}
}
