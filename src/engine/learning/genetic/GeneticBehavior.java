package engine.learning.genetic;

public interface GeneticBehavior
{
    public void mutate(GeneticInformation information, double mutationRate, double mutationInterval);
    public void select(GeneticInformation[] generation, SelectiveFunction function);
    public GeneticInformation crossover();
}