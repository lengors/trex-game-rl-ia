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
    private Thread thread;
    private Game game;

    @Override
    public void setup()
    {
        // Creates trex-game
        game = new TrexGame();

        game.setUPS(60);
        game.addResource(new Loader(this));
        game.addResource(Window.class, this);

        Ground ground = new Ground();
        game.addGameObject(ground);

        // Creates a trex in the game
        TrexObject trex = new TrexObject();
        game.addGameObject(trex);

        Obstacle obstacle = new Pterodactyl();
        game.addGameObject(obstacle);
        
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