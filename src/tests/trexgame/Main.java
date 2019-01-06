package tests.trexgame;

import java.util.Map;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

import engine.utils.Synced;
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
    private static Distribution distribution = Distribution.UNIFORM;
    private SyncedJSON object;
    private Loader loader;

    public synchronized void saveSynced(SyncedJSON object, String path)
    {
        saveJSONObject(object.getValue(), path);
    }

    public NeuralNetwork loadBest()
    {
        JSONArray array = loadJSONArray("best.json");
        Matrix[] weights = new Matrix[array.size()];
        for (int i = 0; i < array.size(); ++i)
        {
            JSONObject json = array.getJSONObject(i);
            int width = json.getInt("width");
            int height = json.getHeight("height");
            Matrix matrix = new Matrix(height, width);
            JSONArray data = json.getJSONArray("data");
            for (int j = 0; j < data.size(); ++j)
                matrix.set(j, data.getDouble(j));
            weights[i] = matrix;
        }
        return new NeuralNetwork(weights);
    }

    public TrexGame newGameBest()
    {
        TrexGame game = new TrexGame();
        game.addResource(ns);
        game.addResource(loader);
        game.addResource(Window.class, window);
        Ground ground = new Ground();
        game.addGameObject(ground);
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
        }).bind(loadBest()));
    }

    public double testBest()
    {
        double sum = 0;
        int nTests = 15;
        for (int i = 0; i < nTests; ++i)
        {
            TrexGame game = newGameBest();
            game.run();
            sum += game.getGameObjects(Ground.class).get(0).getScore();
        }
        return sum / nTests;
    }

    @Override
    public void setup()
    {
        surface.setVisible(false);
        loader = new Loader(this);
        
        // Code used for training:
        /*if (new File(distribution.filename).exists())
            object = new SyncedJSON(loadJSONObject(distribution.filename));
        else
            object = new SyncedJSON();
        
        int numberOfTests = 50;
        int[] numbersOfIterations = new int[] { 50 };
        int[] numbersIndividualsPerGeneration = new int[] { 150 };
        double[] mutationRates = new double[] { 0.3 };
        double[] mutationIntervals = new double[] { 5 };
        MutationGenerator mg = distribution.generator;

        Executor[] executors = new Executor[mutationRates.length];
        for (int i = 0; i < executors.length; ++i)
            executors[i] = new Executor(this, object, loader, numberOfTests, numbersOfIterations, numbersIndividualsPerGeneration, new double[] { mutationRates[i] }, mutationIntervals, mg);
        for (int i = 1; i < executors.length; ++i)
            executors[i].start();
        executors[0].run();
        for (int i = 1; i < executors.length; ++i)
            try
            {
                executors[i].join();
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }*/

        double x = testBest();
        PrintWriter pw = new PrintWriter("score-test.txt");
        pw.println(x);
        
        exit();
        /*fill(0);

        addListener(new DefaultListener()
        {
            @Override
            public void onMouseWheel(MouseEvent event)
            {
                ups += event.getCount();
                if (ups <= 0)
                    ups = 1;
                game.setUPS(ups);
            }
        });*/
    }

    /*@Override
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
        }
    }*/

    public static void main(String[] args)
    {
        Window.build(Main.class, 800, 480).run();
    }

    public static class SyncedJSON extends Synced<JSONObject>
    {
        public SyncedJSON(JSONObject value)
        {
            super(value);
        }

        public SyncedJSON()
        {
            this(new JSONObject());
        }

        public SyncedJSON getJSONObject(String key)
        {
            SyncedJSON synced;
            synchronized (value)
            {
                if (value.hasKey(key))
                    synced = new SyncedJSON(value.getJSONObject(key));
                else
                    synced = null;
            }
            return synced;
        }

        public SyncedJSON setDouble(String key, double value)
        {
            synchronized (this.value)
            {
                this.value.setDouble(key, value);
            }
            return this;
        }

        public SyncedJSON setJSONObject(String key, JSONObject value)
        {
            synchronized (this.value)
            {
                this.value.setJSONObject(key, value);
            }
            return this;
        }

        public SyncedJSON setString(String key, String value)
        {
            synchronized (this.value)
            {
                this.value.setString(key, value);
            }
            return this;
        }
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

    public static enum Distribution
    {
        UNIFORM(MutationGenerator.UNIFORM, "uniform.json"),
        GAUSSIAN(MutationGenerator.GAUSSIAN, "gaussian.json");

        public String filename;
        public MutationGenerator generator;

        private Distribution(MutationGenerator mg, String filename)
        {
            this.generator = mg;
            this.filename = filename;
        }
    }

    public static class Executor extends Thread
    {
        Main window;
        Loader loader;
        SyncedJSON object;
        int numberOfTests;
        MutationGenerator mg;
        double[] mutationRates, mutationIntervals;
        NetworkSelection ns = new NetworkSelection();
        int[] numbersOfIterations, numbersIndividualsPerGeneration;

        public Executor(Main window, SyncedJSON object, Loader loader, int numberOfTests, int[] numbersOfIterations, int[] numbersIndividualsPerGeneration, double[] mutationRates, double[] mutationIntervals, MutationGenerator mg)
        {
            this.mg = mg;
            this.loader = loader;
            this.window = window;
            this.object = object;
            this.numberOfTests = numberOfTests;
            this.mutationRates = mutationRates;
            this.mutationIntervals = mutationIntervals;
            this.numbersOfIterations = numbersOfIterations;
            this.numbersIndividualsPerGeneration = numbersIndividualsPerGeneration;
        }

        @Override
        public void run()
        {
            for (int numberOfIterations : numbersOfIterations)
            {
                SyncedJSON nij = object.getJSONObject(Integer.toString(numberOfIterations));
                if (nij == null)
                    object.setJSONObject(Integer.toString(numberOfIterations), (nij = new SyncedJSON().setString("name", "numberOfIterations")).getValue());
                for (int numberIndividualsPerGeneration : numbersIndividualsPerGeneration)
                {
                    SyncedJSON ngj = nij.getJSONObject(Integer.toString(numberIndividualsPerGeneration));
                    if (ngj == null)
                        nij.setJSONObject(Integer.toString(numberIndividualsPerGeneration), (ngj = new SyncedJSON().setString("name", "numberOfIndividualsPerGeneration")).getValue());
                    for (double mutationRate : mutationRates)
                    {
                        SyncedJSON mtj = ngj.getJSONObject(Double.toString(mutationRate));
                        if (mtj == null)
                            ngj.setJSONObject(Double.toString(mutationRate), (mtj = new SyncedJSON().setString("name", "mutationRate")).getValue());
                        for (double mutationInterval : mutationIntervals)
                        {
                            SyncedJSON mij = mtj.getJSONObject(Double.toString(mutationInterval));
                            // if (mij == null)
                            // {
                                double meanScore = 0;
                                for (int i = 0; i < numberOfTests; ++i)
                                {
                                    int score = newGame(mutationRate, mutationInterval, numberOfIterations, numberIndividualsPerGeneration, mg, window);
                                    System.out.printf("%f %f %d %d: %03.1f%sComplete!\n", mutationRate, mutationInterval, numberOfIterations, numberIndividualsPerGeneration, i * 100.0 / numberOfTests, "%");
                                    meanScore += score;
                                }
                                meanScore /= numberOfTests;
                                System.out.printf("Mean(%f, %d, %d) = %f\n", mutationRate, numberOfIterations, numberIndividualsPerGeneration, meanScore);
                                mtj.setJSONObject(Double.toString(mutationInterval), (mij = new SyncedJSON().setString("name", "mutationInterval").setDouble("value", meanScore)).getValue());
                                // window.saveSynced(object, Main.distribution.filename);
                            // }
                        }
                    }
                }
            }
        }

        public int newGame(double mutationRate, double mutationInterval, int numberOfIterations, int numberIndividualsPerGeneration, MutationGenerator mg, Main window)
        {
            int maxScore = 0;
            GeneticBehavior gb = new DefaultGeneticBehavior(mg);
            Generation generation = new Generation(numberIndividualsPerGeneration, mutationRate, mutationInterval, gb);
            generation.initialize((int index) -> new NeuralNetwork(6, 3).map(Matrix.RANDOMIZE));
            for (int i = 0; i < numberOfIterations; ++i)
            {
                TrexGame game = new TrexGame();
                game.addResource(ns);
                game.addResource(loader);
                game.addResource(Window.class, window);
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
                if (maxScore < ground.getScore())
                {
                    maxScore = ground.getScore();
                    Map.Entry<GeneticInformation, Integer> maxEntry = null;
                    for (Map.Entry<GeneticInformation, Integer> entry : ns.scores.entrySet())
                        if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
                            maxEntry = entry;
                    NeuralNetwork nn = (NeuralNetwork) maxEntry.getKey();
                    Matrix[] weights = nn.get();

                    JSONArray array = new JSONArray();
                    for (Matrix matrix : weights)
                    {
                        JSONObject json = new JSONObject();
                        json.setInt("height", matrix.getHeight());
                        json.setInt("width", matrix.getWidth());
                        JSONArray data = new JSONArray();
                        for (int a = 0; a < matrix.size(); ++a)
                            data.append(matrix.get(a));
                        json.setJSONArray("data", data);
                        array.append(json);
                    }
                    window.saveJSONArray(array, "best.json");
                }
                generation = generation.next(ns);
                ns.clear();
            }
            return maxScore;
        }
    }
}