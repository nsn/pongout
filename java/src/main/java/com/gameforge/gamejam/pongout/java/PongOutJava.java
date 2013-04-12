package com.gameforge.gamejam.pongout.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.gameforge.gamejam.pongout.core.PongOut;

public class PongOutJava {

	public static void main(String[] args) {
		JavaPlatform platform = JavaPlatform.register();
		platform.assets().setPathPrefix(
				"com/gameforge/gamejam/pongout/resources");
		PlayN.run(new PongOut());
	}
}
