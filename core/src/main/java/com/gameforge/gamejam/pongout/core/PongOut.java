package com.gameforge.gamejam.pongout.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.AssetWatcher;
import playn.core.AssetWatcher.Listener;
import playn.core.Game;
import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.Surface;
import pythagoras.f.Rectangle;

import com.gameforge.gamejam.pongout.core.play.ImageStore;
import com.gameforge.gamejam.pongout.core.play.LoadingState;
import com.gameforge.gamejam.pongout.core.play.PlayState;
import com.gameforge.gamejam.pongout.core.play.ResultState;

public class PongOut implements Game {
    public static final int SCREENWIDTH = 1280;
    public static final int SCREENHEIGHT = 768;
    private GameState currentState;
    private PlayNRenderer renderer;
    private float frameAlpha = 1.0f;
    private int lastWinner = 0;

    @Override
    public void init() {
        graphics().ctx().setSize(SCREENWIDTH, SCREENHEIGHT);

        final Image backgroundImage = assets()
                .getImage("images/background.jpg");
        final Image spritesheetImage = assets().getImage(
                "images/spritesheet.png");
        final Image player1WinsImage = assets().getImage("images/p1wins.jpg");
        final Image player2WinsImage = assets().getImage("images/p2wins.jpg");
        graphics().rootLayer()
                .add(graphics().createImageLayer(backgroundImage));

        ImmediateLayer gameLayer = graphics().createImmediateLayer(SCREENWIDTH,
                SCREENHEIGHT, new ImmediateLayer.Renderer() {
                    @Override
                    public void render(Surface surface) {
                        currentState.paint(surface, new Rectangle(0.0f, 0.0f,
                                SCREENWIDTH, SCREENHEIGHT), frameAlpha);
                    }
                });

        graphics().rootLayer().add(gameLayer);

        renderer = new PlayNRenderer();

        AssetWatcher assetWatcher = new AssetWatcher(new Listener() {

            @Override
            public void done() {
                ImageStore.getInstance().addImage("spritesheet",
                        spritesheetImage);
                ImageStore.getInstance()
                        .addImage("background", backgroundImage);
                ImageStore.getInstance().addImage("player1wins",
                        player1WinsImage);
                ImageStore.getInstance().addImage("player2wins",
                        player2WinsImage);
                changeState(GameState.STATE.PLAY);
            }

            @Override
            public void error(Throwable e) {
            }

        });
        assetWatcher.add(backgroundImage);
        assetWatcher.add(spritesheetImage);
        assetWatcher.start();

        changeState(GameState.STATE.LOADING);

    }

    public void changeState(GameState.STATE newState) {
        if (currentState != null) {
            currentState.onExit();
        }
        switch (newState) {
        case PLAY:
            changeState(new PlayState(renderer, this));
            break;
        case RESULT:
            changeState(new ResultState(renderer, this, lastWinner));
            break;
        case LOADING:
            changeState(new LoadingState(renderer, this));
            break;
        default:
            throw new IllegalArgumentException("unable to switch to "
                    + newState);
        }
    }

    private void changeState(GameState state) {
        currentState = state;
        currentState.onEntry();
    }

    @Override
    public void paint(float alpha) {
        frameAlpha = alpha;
    }

    @Override
    public void update(float delta) {
        currentState.update(delta);
    }

    @Override
    public int updateRate() {
        return 0;
    }

    public int getLastWinner() {
        return lastWinner;
    }

    public void setLastWinner(int lastWinner) {
        this.lastWinner = lastWinner;
    }

}
