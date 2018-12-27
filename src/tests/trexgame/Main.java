package tests.trexgame;

import java.awt.event.KeyEvent;

import engine.graphics.Window;

import engine.listeners.KeyListener;
import engine.listeners.MouseListener;

import engine.base.Game;
import engine.base.Observer;
import engine.base.Observable;
import engine.base.DefaultObservable;

import tests.trexgame.objects.Ground;
import tests.trexgame.objects.TrexObject;
import tests.trexgame.objects.ShapedObject;
import tests.trexgame.objects.obstacles.Obstacle;
import tests.trexgame.objects.obstacles.Pterodactyl;

import processing.core.PShape;
import processing.core.PVector;

import processing.event.MouseEvent;

public class Main extends Window
{
    private Loader loader;
    private Thread thread;
    private Game game;

    public void newGame()
    {
        // Creates trex-game
        game = new TrexGame();

        game.addResource(new Loader(this));
        game.addResource(Window.class, this);

        Ground ground = new Ground();
        game.addGameObject(ground);

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

        // Waits for setup to run
        game.waitForSetup();
    }

    @Override
    public void setup()
    {
        loader = new Loader(this);
        fill(0);
        newGame();
    }

    @Override
    public void draw()
    {
        background(255);
        for (ShapedObject shapedObject : game.getGameObjects(ShapedObject.class))
        {
            PShape shape = shapedObject.getShape();
            PVector position = shapedObject.getPosition();    
            if (shapedObject.getClass().equals(Ground.class))
            {
                Ground ground = (Ground) shapedObject;
                shape(shape, position.x + shapedObject.getTexture().width, position.y);
                text(ground.getScore(), 10, 10 + textAscent());
            }
            shape(shape, position.x, position.y);
        }
        if (game.getGameObjects(TrexObject.class).size() == 0)
        {
            try
            {
                game.stop();
                thread.join();
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            newGame();
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
        Window window = Window.build(Main.class, 800, 480);
    }

    public static class Player extends DefaultObservable implements KeyListener
    {
        private int pressedKeyCode;

        @Override
        public void onKeyPress(processing.event.KeyEvent event)
        {
            if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_UP)
                dispatch((TrexObject.Action) TrexObject::jump);
            else if (event.getKeyCode() == KeyEvent.VK_DOWN)
                dispatch((TrexObject.Action) TrexObject::down);
            else
                return;
            pressedKeyCode = event.getKeyCode();
        }

        @Override
        public void onKeyRelease(processing.event.KeyEvent event)
        {
            if (event.getKeyCode() == pressedKeyCode)
            {
                dispatch((TrexObject.Action) (TrexObject trex) -> { });
                pressedKeyCode = -1;
            }
        }
    }
}