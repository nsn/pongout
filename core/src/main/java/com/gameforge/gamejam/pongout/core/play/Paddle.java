package com.gameforge.gamejam.pongout.core.play;

import playn.core.Color;
import pythagoras.f.Vector;

import com.gameforge.gamejam.pongout.core.PongoutSprite;
import com.nightspawn.sg.GroupNode;
import com.nightspawn.sg.Spatial;

public class Paddle extends GroupNode<Spatial> {
	private final PongoutSprite top;
	private PongoutSprite bottom;

	Paddle() {
		top = PongoutSprite.create(32, 32);
		addChild(top);
		setDrawBoundary(true);
		setBoundaryColor(Color.rgb(255, 0, 255));

		translate(new Vector(100, 100));
	}
}
