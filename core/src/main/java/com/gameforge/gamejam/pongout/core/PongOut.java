package com.gameforge.gamejam.pongout.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;
import playn.core.Game;
import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.Surface;
import pythagoras.f.Rectangle;

import com.gameforge.gamejam.pongout.core.play.ImageStore;
import com.gameforge.gamejam.pongout.core.play.LoadingState;
import com.gameforge.gamejam.pongout.core.play.PlayState;
import com.gameforge.gamejam.pongout.core.play.ResultState;
import com.gameforge.gamejam.pongout.core.play.SoundStore;
import com.gameforge.gamejam.pongout.core.play.StartState;
import playn.core.Sound;

public class PongOut implements Game {
    public static final int SCREENWIDTH = 1280;
    public static final int SCREENHEIGHT = 768;
    private GameState currentState;
    private PlayNRenderer renderer;
    private float frameAlpha = 1.0f;
    private int lastWinner = 0;
    private Sound music;
    private int starts = 0;

    @Override
    public void init() {
        graphics().ctx().setSize(SCREENWIDTH, SCREENHEIGHT);

        final Image backgroundImage = assets()
                .getImage("images/background.jpg");
        final Image spritesheetImage = assets().getImage(
                "images/spritesheet.png");
        final Image player1WinsImage = assets().getImage("images/p1wins.jpg");
        final Image player2WinsImage = assets().getImage("images/p2wins.jpg");
        final Image openingScreenImage = assets().getImage(
                "images/openingscreen.jpg");

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

        // AssetWatcher assetWatcher = new AssetWatcher(new Listener() {
        //
        // @Override
        // public void done() {
        // ImageStore.getInstance()
        // .addImage("opening", openingScreenImage);
        // changeState(STATE.LOADING);
        // }
        //
        // @Override
        // public void error(Throwable e) {
        // }
        //
        // });
        // assetWatcher.add(openingScreenImage);
        // assetWatcher.start();

        ImageStore.getInstance().addImage("opening", openingScreenImage);
        ImageStore.getInstance().addImage("spritesheet", spritesheetImage);
        ImageStore.getInstance().addImage("background", backgroundImage);
        ImageStore.getInstance().addImage("player1wins", player1WinsImage);
        ImageStore.getInstance().addImage("player2wins", player2WinsImage);
//        music = assets().getSound("sounds/background");
//        log().info("music " + music);
//        music.prepare();
//        music.play();
//        music.setLooping(true);
        Sound blip = assets().getSound("sounds/blip");
        blip.prepare();
        
        SoundStore.getInstance().addSound("blip", blip);
        Sound damage = assets().getSound("sounds/damage");
        damage.prepare();
        SoundStore.getInstance().addSound("damage", damage);
        Sound brick = assets().getSound("sounds/brick");
        brick.prepare();
        SoundStore.getInstance().addSound("brick", brick);
        Sound powerup = assets().getSound("sounds/powerup");
        powerup.prepare();
        SoundStore.getInstance().addSound("powerup", powerup);
        changeState(GameState.STATE.LOADING);        

    }

    public void changeState(GameState.STATE newState) {
        if (currentState != null) {
            currentState.onExit();
        }
        switch (newState) {
        case PLAY:
            changeState(new PlayState(renderer, this, (starts==0)));
            starts++;
            break;
        case RESULT:
            changeState(new ResultState(renderer, this, lastWinner));
            break;
        case LOADING:
            changeState(new LoadingState(renderer, this));
            break;
        case START:
            changeState(new StartState(renderer, this));
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
