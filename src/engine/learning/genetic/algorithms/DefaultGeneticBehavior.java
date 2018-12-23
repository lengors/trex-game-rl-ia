package engine.learning.genetic.algorithms;

import java.util.List;
import java.util.ArrayList;

import engine.learning.NeuralNetwork;

import engine.learning.genetic.GeneticBehavior;
import engine.learning.genetic.SelectiveFunction;
import engine.learning.genetic.GeneticInformation;

import engine.learning.genetic.utils.MutationGenerator;

import engine.math.Matrix;

import engine.utils.Pair;

public class DefaultGeneticBehavior implements GeneticBehavior
{
    private List<Pair<Double, GeneticInformation>> generation = new ArrayList<>();
    private MutationGenerator generator;

    public DefaultGeneticBehavior(MutationGenerator generator)
    {
        this.generator = generator;
    }

    public DefaultGeneticBehavior()
    {
        this(MutationGenerator.UNIFORM);
    }

    @Override
    public void select(GeneticInformation[] generation, SelectiveFunction function)
    {
        this.generation.clear();
        for (GeneticInformation information : generation)
            this.generation.add(new Pair<>(function.fitness(information), information));
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
        NeuralNetwork network = new NeuralNetwork(layers);
        for (Matrix matrix : network.get())
            matrix.map((double weight) ->
            {
                return weight;
            });
        return network;
    }
}