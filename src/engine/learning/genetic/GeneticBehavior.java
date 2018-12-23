package engine.learning.genetic;

public interface GeneticBehavior
{
    public void select(GeneticInformation[] generation, SelectiveFunction function);
    public void mutate(GeneticInformation information, double mutationRate);
    public GeneticInformation crossover();
}