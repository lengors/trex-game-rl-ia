package engine.learning.genetic;

import java.util.List;
import java.util.ArrayList;

import engine.learning.genetic.algorithms.DefaultGeneticBehavior;

public class Generation
{
    private double mutationRate, mutationInterval;
    private GeneticBehavior geneticBehavior;
    private GeneticInformation[] generation;

    public Generation(int amount, double mutationRate, double mutationInterval, GeneticBehavior geneticBehavior)
    {
        generation = new GeneticInformation[amount];
        this.mutationInterval = mutationInterval;
        this.geneticBehavior = geneticBehavior;
        this.mutationRate = mutationRate;
    }

    public Generation(int amount, double mutationRate, double mutationInterval)
    {
        this(amount, mutationRate, mutationInterval, new DefaultGeneticBehavior());
    }

    public GeneticInformation[] information()
    {
        return generation;
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
            geneticBehavior.mutate(information, mutationRate, mutationInterval);
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