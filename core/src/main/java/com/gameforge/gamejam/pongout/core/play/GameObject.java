package com.gameforge.gamejam.pongout.core.play;

import com.nightspawn.sg.BoundingRectangle;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Node;

public class GameObject extends GroupNode<Node> {
	BoundingRectangle oldBoundingRectangle;

	@Override
	public void update(float deltaMs) {
		super.update(deltaMs);
		// store old worldbound

	}
}
