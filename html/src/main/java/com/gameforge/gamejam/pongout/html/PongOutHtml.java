package com.gameforge.gamejam.pongout.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.gameforge.gamejam.pongout.core.PongOut;

public class PongOutHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("pongout/");
    PlayN.run(new PongOut());
  }
}
