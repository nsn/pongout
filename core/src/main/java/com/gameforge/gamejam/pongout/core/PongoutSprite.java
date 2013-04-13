package com.gameforge.gamejam.pongout.core;

import com.gameforge.gamejam.pongout.core.play.ImageStore;
import static playn.core.PlayN.assets;
import playn.core.Image;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;

import com.nightspawn.sg.Sprite;

public class PongoutSprite extends Sprite<Image> {
	public static final String SPRITESHEET = "spritesheet";

	public PongoutSprite(Image texture, Dimension d) {
		super(texture, d);
	}

	public PongoutSprite(Image texture, Dimension d, Vector offset) {
		super(texture, d, offset);
	}

	public PongoutSprite(Dimension d) {
		super(ImageStore.getInstance().getImage(SPRITESHEET), d);
	}

	public PongoutSprite(Dimension d, Vector offset) {
		super(ImageStore.getInstance().getImage(SPRITESHEET), d, offset);
	}

	public static PongoutSprite create(float w, float h) {
		return create(w, h, 0, 0);
	}

	public static PongoutSprite create(float w, float h, float xoff, float yoff) {
        Image t = ImageStore.getInstance().getImage(SPRITESHEET);
		Dimension dims = new Dimension(w, h);
		Vector offset = new Vector(xoff, yoff);

		return new PongoutSprite(t, dims, offset);
	}

}
