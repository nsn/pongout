package com.gameforge.gamejam.pongout.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;
import playn.html.HtmlPlatform.Configuration;

import com.gameforge.gamejam.pongout.core.PongOut;

public class PongOutHtml extends HtmlGame {

    @Override
    public void start() {
        Configuration cfg = new Configuration();

        HtmlPlatform platform = HtmlPlatform.register(cfg);
        platform.assets().setPathPrefix("pongout/");
        PlayN.run(new PongOut());
    }
}
