package engine.learning.genetic.utils;

import engine.math.Matrix;

import engine.utils.Generator;
import engine.utils.UniformGenerator;
import engine.utils.GaussianGenerator;

public abstract class MutationGenerator implements Generator<Matrix.Map>
{
    public final static MutationGenerator UNIFORM = new MutationGenerator(new UniformGenerator())
    {
        @Override
        public Matrix.Map next()
        {
            return (double weight) -> getRandomGenerator().next() < getMutationRate()
                    ? weight + getRandomGenerator().next() * getMutationInterval() - 1
                    : weight;
        }
    };

    public final static MutationGenerator GAUSSIAN = new MutationGenerator(new GaussianGenerator())
    {
        @Override
        public Matrix.Map next()
        {
            return (double weight) -> getRandomGenerator().next() < getMutationRate()
                                    ? weight + getRandomGenerator().next() * getMutationInterval() - 1
                                    : weight;
        }
    };

    private double mutationRate, mutationInterval;
    private Generator<Double> randomGenerator;

    public MutationGenerator(Generator<Double> randomGenerator)
    {
        this.randomGenerator = randomGenerator;
    }

    public Generator<Double> getRandomGenerator()
    {
        return randomGenerator;
    }

    public double getMutationRate()
    {
        return mutationRate;
    }

    public double getMutationInterval()
    {
        return mutationInterval;
    }

    public void setMutationRate(double mutationRate)
    {
        this.mutationRate = mutationRate;
    }

    public void setMutationInterval(double mutationInterval)
    {
        this.mutationInterval = mutationInterval;
    }
}