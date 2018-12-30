package engine.learning.genetic;

import java.util.List;
import java.util.ArrayList;

import engine.learning.genetic.algorithms.DefaultGeneticBehavior;

public class Generation
{
    private GeneticBehavior geneticBehavior;
    private GeneticInformation[] generation;
    private double mutationRate;

    public Generation(int amount, double mutationRate, GeneticBehavior geneticBehavior)
    {
        generation = new GeneticInformation[amount];
        this.geneticBehavior = geneticBehavior;
        this.mutationRate = mutationRate;
    }

    public Generation(int amount, double mutationRate)
    {
        this(amount, mutationRate, new DefaultGeneticBehavior());
    }

    public <T> List<T> information()
    {
        List<T> result = new ArrayList<>(generation.length);
        for (GeneticInformation info : generation)
            result.add((T) info);
        return result;
    }

    public GeneticInformation[] initialize(Initializer initializer)
    {
        for (int i = 0; i < generation.length; ++i)
            generation[i] = initializer.init(i);
        return generation;
    }

    public Generation next(SelectiveFunction function)
    {
        GeneticInformation[] nextGeneration = new GeneticInformation[generation.length];
        geneticBehavior.select(generation, function);
        for (int i = 0; i < nextGeneration.length; )
        {
            GeneticInformation information = geneticBehavior.crossover();
            geneticBehavior.mutate(information, mutationRate);
            nextGeneration[i++] = information;
        }
        generation = nextGeneration;
        return this;
    }

    public static interface Initializer
    {
        public GeneticInformation init(int index);
    }
}