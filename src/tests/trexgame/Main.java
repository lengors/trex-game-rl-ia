package tests.trexgame;

import java.awt.event.KeyEvent;

import engine.graphics.Window;

import engine.listeners.KeyListener;
import engine.listeners.MouseListener;

import engine.base.Game;
import engine.base.Observer;
import engine.base.Observable;
import engine.base.DefaultObservable;

import tests.trexgame.objects.TrexObject;
import tests.trexgame.objects.ShapedObject;

import processing.core.PShape;
import processing.core.PVector;

import processing.event.MouseEvent;

public class Main extends Window
{
    private Thread thread;
    private Game game;

    @Override
    public void setup()
    {
        // Creates trex-game
        game = new TrexGame();
        
        game.setUPS(2);
        game.addResource(new Loader(this));
        game.addResource(Window.class, this);

        // Creates a trex in the game
        TrexObject trex = new TrexObject();
        game.addGameObject(trex);
        
        // Creates player
        Player player = new Player();
        
        // Binds observers to observables
        player.addObserver(trex);
        
        // Binds listeners this window
        addListener(player);
        addListener(game);

        // starts game
        thread = game.makeThreadable();
        thread.start();
        game.waitForSetup();
    }

    @Override
    public void draw()
    {
        background(255);
        for (ShapedObject shapedObject : game.getGameObjects(ShapedObject.class))
        {
            PShape shape = shapedObject.getShape();
            PVector position = shapedObject.getPosition();    
            shape(shape, position.x, position.y);
        }
    }

    @Override
    public void dispose()
    {
        super.dispose();
        try
        {
            game.stop();
            thread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        Window window = Window.build(Main.class, 400, 400);
    }

    public static class Player extends DefaultObservable implements MouseListener
    {
        @Override public void onMouseMove(MouseEvent event) { dispatch(new PVector(event.getX(), event.getY())); }
        @Override public void onMouseDrag(MouseEvent event) { }
        @Override public void onMousePress(MouseEvent event) { }
        @Override public void onMouseClick(MouseEvent event) { }
        @Override public void onMouseWheel(MouseEvent event) { }
        @Override public void onMouseRelease(MouseEvent event) { }
    }
    
    /* implements KeyListener
    {
        private int pressedKeyCode;

        @Override
        public void onKeyPress(processing.event.KeyEvent event)
        {
            if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_UP)
                dispatch(1Trex.JUMP);
            else if (event.getKeyCode() == KeyEvent.VK_DOWN)
                dispatch(2Trex.DOWN);
            else
                return;
            pressedKeyCode = event.getKeyCode();
        }

        @Override
        public void onKeyRelease(processing.event.KeyEvent event)
        {
            if (event.getKeyCode() == pressedKeyCode)
            {
                dispatch(0Trex.NOACTION);
                pressedKeyCode = -1;
            }
        }
    }*/
}