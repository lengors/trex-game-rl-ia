package tests.trexgame;

import java.awt.event.KeyEvent;

import engine.utils.Utils;

import engine.graphics.Window;

import engine.learning.NeuralNetwork;
import engine.learning.genetic.Generation;
import engine.learning.genetic.algorithms.DefaultGeneticBehavior;

import engine.math.Matrix;

import engine.listeners.KeyListener;

import engine.base.Game;
import engine.base.DefaultObservable;

import tests.trexgame.objects.Ground;
import tests.trexgame.objects.TrexObject;
import tests.trexgame.objects.ShapedObject;

import tests.trexgame.objects.obstacles.Obstacle;

import processing.core.PShape;
import processing.core.PVector;

public class Main extends Window
{
    private Generation generation;
    private Loader loader;
    private Thread thread;
    private Game game;

    public void newGame()
    {
        // Creates trex-game
        game = new TrexGame();

        game.addResource(loader);
        game.addResource(Window.class, this);

        Ground ground = new Ground();
        game.addGameObject(ground);

        for (NeuralNetwork gi : generation.<NeuralNetwork>information())
            game.addGameObject(new TrexObject((TrexObject trex) ->
            {
                Obstacle obstacle = trex.getObstacle();
                if (obstacle != null)
                {
                    PVector vector = PVector.sub(obstacle.getPosition(), trex.getPosition());
                    double[] output = gi.get(vector.x, vector.y, obstacle.getWidth(), obstacle.getHeight());
                    int max = Utils.max(output);
                    if (max == 0)
                        return (TrexObject.Action) TrexObject::jump;
                    else if (max == 1)
                        return (TrexObject.Action) TrexObject::down;
                }
                return (TrexObject.Action) (TrexObject t) -> { };
            }));

        // starts game
        thread = game.makeThreadable();
        thread.start();

        // Waits for setup to run
        game.waitForSetup();
    }

    @Override
    public void setup()
    {
        generation = new Generation(20, 0.001);
        generation.initialize((int index) -> new NeuralNetwork(4, 3).map(Matrix.RANDOMIZE));

        surface.setVisible(true);
        loader = new Loader(this);
        newGame();
        fill(0);
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
    	Window.build(Main.class, 800, 480);
    }

    /*public static class Player extends DefaultObservable implements KeyListener
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
    }*/
}