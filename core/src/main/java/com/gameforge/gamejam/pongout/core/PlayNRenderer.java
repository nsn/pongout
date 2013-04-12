package com.gameforge.gamejam.pongout.core;

import playn.core.Image;
import playn.core.PlayN;
import playn.core.Surface;
import pythagoras.f.AffineTransform;
import pythagoras.f.Line;
import pythagoras.f.Rectangle;

import com.nightspawn.sg.Area;
import com.nightspawn.sg.BoundingRectangle;
import com.nightspawn.sg.Framerate;
import com.nightspawn.sg.Node;
import com.nightspawn.sg.Scene;
import com.nightspawn.sg.SceneGraphVisitor;
import com.nightspawn.sg.Sprite;

public class PlayNRenderer implements SceneGraphVisitor<Image> {

	private Surface currentTarget;
	private BoundingRectangle viewPort;
	private Framerate frameRate;
	private float frameAlpha;

	public PlayNRenderer() {
		frameRate = new Framerate(PlayN.currentTime());
	}

	public void render(Scene s, Rectangle v, Surface target, float alpha) {
		frameAlpha = alpha;
		frameRate.onPaint(PlayN.currentTime());
		// PlayN.log().debug("asdf " + frameRate.framerate());
		currentTarget = target;
		viewPort = new BoundingRectangle(v);
		s.getRoot().visit(this);
	}

	@Override
	public boolean visitNode(Node n) {
		boolean rendered = isRendered(n);
		if (rendered) {
			// set target's transform
			setTransform(n);
			drawBoundary(n);
			return true;
		} else {
			return rendered;
		}
	}

	@Override
	public void visitArea(Area a) {
		// render at all?
		if (!isRendered(a)) {
			return;
		}
		setTransform(a);
		drawBoundary(a);
	}

	@Override
	public void visitSprite(Sprite<Image> s) {
		// render at all?
		if (!isRendered(s)) {
			return;
		}

		// set target's transform
		setTransform(s);

		// render sprite
		float w = s.getDim().width;
		float h = s.getDim().height;

		Image img = s.getTexture();
		// TODO: store subimage in sprite?
		currentTarget.drawImage(
				img.subImage(s.getFrame() * w + s.getOffset().x,
						s.getAnimation() * h + s.getOffset().y, w, h), 0.0f,
				0.0f);
		// currentTarget.drawImage(img, 0.0f, 0.0f, w, h, s.getFrame() * w,
		// s.getAnimation() * h, w, h);

		// draw boundary?
		drawBoundary(s);
	}

	public void setTransform(Node e) {
		// set target's transform
		AffineTransform t = e.getWorldTransform();
		currentTarget.setTransform(t.m00, t.m01, t.m10, t.m11, t.tx, t.ty);
	}

	/**
	 * draws a node's boundary, expects the transform to already be applied
	 * 
	 * @param e
	 */
	public void drawBoundary(Node e) {
		if (!e.drawBoundary()) {
			return;
		}
		// draw modelbound, because target's transform is already set
		BoundingRectangle b = e.getModelBound();
		drawBoundary(b, e.boundaryColor());
	}

	public void drawBoundary(BoundingRectangle b, int color) {
		for (Line l : b.getOutlines()) {
			currentTarget.setFillColor(color);
			currentTarget.drawLine(l.x1, l.y1, l.x2, l.y2, 1.0f);
		}
	}

	private boolean isRendered(Node e) {
		return (e.isRendered() && e.getWorldBound().intersects(viewPort));
	}
}
