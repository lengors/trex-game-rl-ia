package tests.trexgame;

import java.util.Map;
import java.util.HashMap;

import engine.utils.Utils;

import engine.graphics.Window;

import engine.learning.NeuralNetwork;

import engine.learning.genetic.Generation;
import engine.learning.genetic.SelectiveFunction;
import engine.learning.genetic.GeneticInformation;
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

import processing.event.KeyEvent;

public class Main extends Window
{
    private NetworkSelection ns = new NetworkSelection();
    private Generation generation;
    private Loader loader;
    private Thread thread;
    private Game game;

    public void newGame()
    {
        // Creates trex-game
        game = new TrexGame();
        game.setUPS(120);

        game.addResource(ns);
        game.addResource(loader);
        game.addResource(Window.class, this);

        Ground ground = new Ground();
        game.addGameObject(ground);

        for (GeneticInformation gi : generation.information())
            game.addGameObject(new TrexObject((TrexObject trex) ->
            {
                Obstacle obstacle = trex.getObstacle();
                NeuralNetwork nn = trex.get(NeuralNetwork.class);
                if (obstacle != null)
                {
                    PVector position = trex.getPosition();
                    PVector vector = PVector.sub(obstacle.getPosition(), position);
                    double[] output = gi.get(new double[] { ground.getSpeed(), position.y, vector.x, vector.y, obstacle.getWidth(), obstacle.getHeight() });
                    int max = Utils.max(output);
                    if (max == 0)
                        return (TrexObject.Action) TrexObject::jump;
                    else if (max == 1)
                        return (TrexObject.Action) TrexObject::down;
                }
                return (TrexObject.Action) (TrexObject t) -> { };
            }).bind(gi));

        // starts game
        thread = game.makeThreadable();
        thread.start();

        // Waits for setup to run
        game.waitForSetup();
    }

    @Override
    public void setup()
    {
        generation = new Generation(30, 0.01, 0.01);
        generation.initialize((int index) -> new NeuralNetwork(6, 3).map(Matrix.RANDOMIZE));
        surface.setVisible(true);
        loader = new Loader(this);
        newGame();
        fill(0);

        addListener(new KeyListener()
        {
            @Override
            public void onKeyPress(KeyEvent event)
            {
                if (event.getKeyCode() == 's' || event.getKeyCode() == 'S')
                {
                    GeneticInformation[] infos = generation.information();
                    for (int i = 0; i < infos.length; ++i)
                    {
                        System.out.printf("NN %d:\n", i);
                        System.out.println(infos[i]);
                    }
                }
            }

            @Override
            public void onKeyRelease(KeyEvent event)
            {

            }
        });
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
                generation = generation.next(ns);
                ns.clear();
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

    public static class NetworkSelection implements SelectiveFunction
    {
        private Map<GeneticInformation, Integer> scores = new HashMap<>();

        public void clear()
        {
            scores.clear();
        }

        @Override
        public double fitness(GeneticInformation gi)
        {
            return scores.get(gi);
        }

        public void setScore(GeneticInformation gi, int score)
        {
            scores.put(gi, score);
        }
    }
}