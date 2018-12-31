package engine.learning.genetic.algorithms;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

import engine.learning.NeuralNetwork;

import engine.learning.genetic.GeneticBehavior;
import engine.learning.genetic.SelectiveFunction;
import engine.learning.genetic.GeneticInformation;

import engine.learning.genetic.utils.MutationGenerator;

import engine.utils.Pair;
import engine.utils.UniformGenerator;

import engine.math.Matrix;

public class DefaultGeneticBehavior implements GeneticBehavior
{
    private List<Pair<Double, GeneticInformation>> generation = new ArrayList<>();
    private UniformGenerator random = new UniformGenerator();
    private MutationGenerator generator;

    public DefaultGeneticBehavior(MutationGenerator generator) {
        this.generator = generator;
    }

    public DefaultGeneticBehavior()
    {
        this(MutationGenerator.UNIFORM);
    }

    @Override
    public void select(GeneticInformation[] generation, SelectiveFunction function)
    {
        double total = 0;
        this.generation.clear();
        Map<Double, Integer> fitnessScores = new HashMap<>();
        for (GeneticInformation information : generation)
        {
            double fitness = function.fitness(information);
            this.generation.add(new Pair<>(fitness, information));
            Integer counter = fitnessScores.get(fitness);
            if (counter != null)
                fitnessScores.put(fitness, counter + 1);
            else
            {
                total += fitness;
                fitnessScores.put(fitness, 1);
            }
        }
        for (Pair<Double, GeneticInformation> pair : this.generation)
            pair.setKey(pair.getKey() / (total * fitnessScores.get(pair.getKey())));
        Collections.sort(this.generation, (Pair<Double, GeneticInformation> p1, Pair<Double, GeneticInformation> p2) ->
        {
            if (p1.getKey() < p2.getKey())
                return -1;
            if (p1.getKey() > p2.getKey())
                return 1;
            return 0;
        });
    }

    @Override
    public void mutate(GeneticInformation information, double mutationRate, double mutationInterval)
    {
        generator.setMutationRate(mutationRate);
        generator.setMutationInterval(mutationInterval);
        Matrix.Map map = generator.next();
        NeuralNetwork network = (NeuralNetwork) information;
        for (Matrix matrix : network.get())
            matrix.map(map);
    }

    @Override
    public GeneticInformation crossover()
    {
        Pair<Double, GeneticInformation> p1 = random();
        Pair<Double, GeneticInformation> p2 = random();
        NeuralNetwork n1 = (NeuralNetwork) p1.getValue();
        NeuralNetwork n2 = (NeuralNetwork) p2.getValue();
        double total = p1.getKey() + p2.getKey();
        double probabilityP1 = p1.getKey() / total;
        NeuralNetwork network = new NeuralNetwork(n1.layers());
        for (int i = 0; i < network.size(); ++i)
        {
            Matrix matrix = network.get(i);
            for (int j = 0; j < matrix.size(); ++j)
                matrix.set(j, random.next() < probabilityP1 ? n1.get(i).get(j) : n2.get(i).get(j));
        }
        return network;
    }

    public Pair<Double, GeneticInformation> random()
    {
        int index = 0;
        double choice = random.next();
        while (index < generation.size() - 1 && choice >= generation.get(index).getKey())
            choice -= generation.get(index++).getKey();
        return generation.get(index);
    }
}