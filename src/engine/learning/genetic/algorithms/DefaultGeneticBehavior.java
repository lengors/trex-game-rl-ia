package engine.learning.genetic.algorithms;

import java.util.List;
import java.util.ArrayList;

import engine.learning.NeuralNetwork;

import engine.learning.genetic.GeneticBehavior;
import engine.learning.genetic.SelectiveFunction;
import engine.learning.genetic.GeneticInformation;

import engine.learning.genetic.utils.UniformGenerator;
import engine.learning.genetic.utils.MutationGenerator;

import engine.math.Matrix;

public class DefaultGeneticBehavior implements GeneticBehavior
{
    private List<Pair<Double, GeneticInformation>> generation = new ArrayList<>();
    private MutationGenerator generator;
    private UniformGenerator random;

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
        for (GeneticInformation information : generation)
        {
            double fitness = function.fitness(information);
            this.generation.add(new Pair<>(fitness, information));
            total += fitness;
        }
        for (Pair<Double, GeneticInformation> pair : this.generation)
            pair.setKey(pair.getKey() / total);
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
    public void mutate(GeneticInformation information, double mutationRate)
    {
        generator.setMutationRate(mutationRate);
        Matrix.Map map = generator.next();
        NeuralNetwork network = (NeuralNetwork) information;
        for (Matrix matrix : network.get())
            matrix.map(map);
    }

    @Override
    public GeneticInformation crossover()
    {
        Pair<Double, NeuralNetwork> p1 = random();
        Pair<Double, NeuralNetwork> p2 = random();
        double total = p1.getKey() + p2.getKey();
        double probabilityP1 = p1.getKey() / total;
        NeuralNetwork network = new NeuralNetwork(p1.layers());
        for (int i = 0; i < network.size(); ++i)
        {
            Matrix matrix = network.get(i);
            for (int j = 0; j < matrix.size(); ++j)
                matrix.set(i, random.next() < probabilityP1 ? p1.get(i).get(j) : p2.get(i).get(j));
        }
        return network;
    }

    public Pair<Double, NeuralNetwork> random()
    {
        int index = 0;
        double choice = random.next();
        while (index < generation.size() - 1 && choice >= generation.get(index).getKey())
            choice -= generation.get(index++).getKey();
        return generation.get(index);
    }
}