package engine.learning.genetic;

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
}