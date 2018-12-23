package engine.learning.genetic.algorithms;

import java.util.Map;
import java.util.HashMap;

import engine.learning.genetic.GeneticBehavior;
import engine.learning.genetic.SelectiveFunction;
import engine.learning.genetic.GeneticInformation;

public class DefaultGeneticBehavior implements GeneticBehavior
{
    private Map<Integer, GeneticInformation> generation = new HashMap<>();
    private Random random = new Random();

    public void select(GeneticInformation[] generation, SelectiveFunction function)
    {
        this.generation.clear();
        for (GeneticInformation information : generation)
            this.generation.put(function.fitness(information), information);
    }

    public void mutate(GeneticInformation information, double mutationRate)
    {
        Matrix.Map map = (double weight) -> random() < mutationRate ? weight + random() : weight;
        NeuralNetwork network = information.get<NeuralNetwork>();
        for (Matrix matrix : network.get())
            matrix.map(map);
    }

    public GeneticInformation crossover()
    {

    }

    public double random()
    {
        
    }
}