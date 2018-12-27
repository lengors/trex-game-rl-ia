package tests.trexgame;

import java.awt.event.KeyEvent;

import engine.graphics.Window;

import engine.listeners.KeyListener;

import engine.base.DefaultObservable;
import engine.base.Observable;
import engine.base.Observer;
import engine.base.Game;

public class Main extends Window
{
    private Thread thread;
    private Game game;

    @Override
    public void setup()
    {
        // Creates trex-game
        game = new TrexGame();

        /* // Creates a trex in the game
        Trex trex = game.addGameObject(Trex.class);

        // Creates player
        Player player = new Player();
     
        // Binds observers to observables
        player.addObserver(trex);
     
        // Binds listeners this window
        addListener(player); */
        addListener(game);

        // starts game
        thread = game.makeThreadable();
        thread.start();
    }

    @Override
    public void draw()
    {
        background(255);
        /* for (ShapedObject shapedObject : game.getGameObjects(ShapedObject.class))
            shape(shapedObject); */
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
        // Window window = Window.build(Main.class, 400, 400);
    }

    public static class Player extends DefaultObservable implements KeyListener
    {
        private int pressedKeyCode;

        @Override
        public void onKeyPress(processing.event.KeyEvent event)
        {
            if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_UP)
                dispatch(1/*Trex.JUMP*/);
            else if (event.getKeyCode() == KeyEvent.VK_DOWN)
                dispatch(2/*Trex.DOWN*/);
            else
                return;
            pressedKeyCode = event.getKeyCode();
        }

        @Override
        public void onKeyRelease(processing.event.KeyEvent event)
        {
            if (event.getKeyCode() == pressedKeyCode)
            {
                dispatch(0/*Trex.NOACTION*/);
                pressedKeyCode = -1;
            }
        }
    }
}