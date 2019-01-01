package tests.trexgame;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

import engine.utils.Utils;

import engine.graphics.Window;

import engine.learning.NeuralNetwork;

import engine.learning.genetic.Generation;
import engine.learning.genetic.GeneticBehavior;
import engine.learning.genetic.SelectiveFunction;
import engine.learning.genetic.GeneticInformation;
import engine.learning.genetic.algorithms.DefaultGeneticBehavior;

import engine.learning.genetic.utils.MutationGenerator;

import engine.math.Matrix;

import engine.listeners.KeyListener;
import engine.listeners.DefaultListener;

import engine.base.Game;
import engine.base.DefaultObservable;

import tests.trexgame.states.MainState;

import tests.trexgame.objects.Ground;
import tests.trexgame.objects.TrexObject;
import tests.trexgame.objects.ShapedObject;

import tests.trexgame.objects.obstacles.Obstacle;
import tests.trexgame.objects.obstacles.Pterodactyl;

import processing.core.PShape;
import processing.core.PVector;

import processing.event.KeyEvent;
import processing.event.MouseEvent;

import processing.data.JSONArray;
import processing.data.JSONObject;

public class Main extends Window
{
    private NetworkSelection ns = new NetworkSelection();
    private Generation generation;
    private Player player;
    private Loader loader;
    private Thread thread;
    private int ups = 240;
    private Game game;

    public void newGame()
    {
        int numberOfTests = 100;
        int[] numbersOfIterations = new int[] { 5/*, 10, 20, 50, 70, 100, 150, 200*/ };
        int[] numbersIndividualsPerGeneration = new int[] { 5/*, 10, 20, 50, 70, 100, 150, 200*/ };
        double[] mutationRates = new double[] { 0.001, 0.003/*, 0.005, 0.01, 0.03, 0.05, 0.1, 0.3, 0.5*/ };
        double[] mutationIntervals = new double[] { 0.001, 0.003/*, 0.005, 0.01, 0.03, 0.05, 0.1, 0.3, 0.5, 1, 3, 5*/ };
        MutationGenerator mg = MutationGenerator.UNIFORM;

        JSONObject uniformJSON = new JSONObject();
        for (int numberOfIterations : numbersOfIterations)
        {
            JSONObject nij = new JSONObject();
            nij.setString("name", "numberOfIterations");
            uniformJSON.setJSONObject(Integer.toString(numberOfIterations), nij);
            for (int numberIndividualsPerGeneration : numbersIndividualsPerGeneration)
            {
                JSONObject ngj = new JSONObject();
                ngj.setString("name", "numberOfIndividualsPerGeneration");
                nij.setJSONObject(Integer.toString(numberIndividualsPerGeneration), ngj);
                for (double mutationRate : mutationRates)
                {
                    JSONObject mtj = new JSONObject();
                    mtj.setString("name", "mutationRate");
                    ngj.setJSONObject(Double.toString(mutationRate), mtj);
                    for (double mutationInterval : mutationIntervals)
                    {
                        JSONObject mij = new JSONObject();
                        mij.setString("name", "mutationInterval");
                        mtj.setJSONObject(Double.toString(mutationInterval), mij);
                        double meanScore = 0;
                        for (int i = 0; i < numberOfTests; ++i)
                        {
                            int score = newGame(mutationRate, mutationInterval, numberOfIterations, numberIndividualsPerGeneration, mg);
                            System.out.printf("%f %f %d %d: %03.1f%sComplete!\n", mutationRate, mutationInterval, numberOfIterations, numberIndividualsPerGeneration, i * 100.0 / numberOfTests, "%");
                            meanScore += score;
                        }
                        meanScore /= numberOfTests;
                        System.out.printf("Mean(%f, %d, %d) = %f\n", mutationRate, numberOfIterations, numberIndividualsPerGeneration, meanScore);
                        mij.setDouble("value", meanScore);
                        saveJSONObject(uniformJSON, "uniform.json");
                    }
                }
            }
        }
    }

    public int newGame(double mutationRate, double mutationInterval, int numberOfIterations, int numberIndividualsPerGeneration, MutationGenerator mg)
    {
        int maxScore = 0;
        GeneticBehavior gb = new DefaultGeneticBehavior(mg);
        generation = new Generation(numberIndividualsPerGeneration, mutationRate, mutationInterval, gb);
        generation.initialize((int index) -> new NeuralNetwork(6, 3).map(Matrix.RANDOMIZE));
        for (int i = 0; i < numberOfIterations; ++i)
        {

            game = new TrexGame();
            game.addResource(ns);
            game.addResource(loader);
            game.addResource(Window.class, this);

            Ground ground = new Ground();
            game.addGameObject(ground);

            for (GeneticInformation gi : generation.information())
                game.addGameObject(new TrexObject((TrexObject trex) ->
                {
                    Obstacle obstacle = ((TrexGame) trex.getGame()).getObstacle();
                    if (obstacle != null)
                    {
                        NeuralNetwork nn = trex.get(NeuralNetwork.class);
                        PVector position = trex.getPosition();
                        double[] output = gi.get(Utils.normalize(ground.getSpeed(), trex.getGroundPosition() - position.y, obstacle.getPosition().x - position.x, obstacle.getGroundPosition() - obstacle.getPosition().y, obstacle.getWidth(), obstacle.getHeight()));
                        int max = Utils.max(output);
                        if (max == 0)
                            return (TrexObject.Action) TrexObject::jump;
                        else if (max == 1)
                            return (TrexObject.Action) TrexObject::down;
                    }
                    return (TrexObject.Action) (TrexObject t) -> { };
                }).bind(gi));

            game.run();
            generation = generation.next(ns);
            ns.clear();
            if (maxScore < ground.getScore())
                maxScore = ground.getScore();
        }
        return maxScore;
    }

    @Override
    public void setup()
    {
        surface.setVisible(true);
        loader = new Loader(this);
        newGame();
        exit();
        fill(0);

        addListener(new DefaultListener()
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
                else if (event.getKeyCode() == 'm' || event.getKeyCode() == 'M')
                    System.out.println(MainState.getMaxScore());
            }

            @Override
            public void onMouseWheel(MouseEvent event)
            {
                ups += event.getCount();
                if (ups <= 0)
                    ups = 1;
                game.setUPS(ups);
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

        text(ups, 10, 10 + textAscent() * 2 + textDescent());
        
        Obstacle obstacle = ((TrexGame) game).getObstacle();
        if (obstacle != null)
        {
            noFill();
            stroke(255, 0, 0);
            rect(obstacle.getPosition().x - obstacle.getWidth() / 2, obstacle.getPosition().y - obstacle.getHeight() / 2, obstacle.getWidth(), obstacle.getHeight());
            fill(0);
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
        /*
        try
        {
            game.stop();
            thread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }*/
    }

    public static void main(String[] args)
    {
        Window.build(Main.class, 800, 480).run();
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

    public static class Player extends DefaultObservable implements KeyListener
    {
        private int keyPressedCode = 0;

        @Override
        public void onKeyPress(KeyEvent event)
        {
            if (event.getKeyCode() == java.awt.event.KeyEvent.VK_UP || event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE)
                dispatch((TrexObject.Action) TrexObject::jump);
            else if (event.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN)
                dispatch((TrexObject.Action) TrexObject::down);
            else
                return;
            keyPressedCode = event.getKeyCode();
        }

        @Override
        public void onKeyRelease(KeyEvent event)
        {
            if (event.getKeyCode() == keyPressedCode)
            {
                dispatch((TrexObject.Action) (TrexObject trex) -> { });
                keyPressedCode = 0;
            }
        }
    }
}