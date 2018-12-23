package engine.learning.genetic.algorithms;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;

import engine.learning.NeuralNetwork;

import engine.learning.genetic.GeneticBehavior;
import engine.learning.genetic.SelectiveFunction;
import engine.learning.genetic.GeneticInformation;

import engine.learning.genetic.utils.MutationGenerator;

public class DefaultGeneticBehavior implements GeneticBehavior
{
    private Map<Double, GeneticInformation> generation = new HashMap<>();
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
            this.generation.put(function.fitness(information), information);
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
        return null;
    }
}