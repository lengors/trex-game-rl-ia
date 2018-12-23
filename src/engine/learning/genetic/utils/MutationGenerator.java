package engine.learning.genetic.utils;

import engine.utils.Generator;
import engine.utils.UniformGenerator;
import engine.utils.GaussianGenerator;

public class MutationGenerator implements Generator<Matrix.Map>
{
    public final static MutationGenerator UNIFORM = new MutationGenerator()
    {
        public MutationGenerator()
        {
            super(new UniformGenerator());
        }

        @Override
        public Matrix.Map next()
        {
            return (double weight) -> getRandomGenerator().next() < getMutationRate()
                    ? weight + getRandomGenerator().next() * 2 - 1
                    : weight;
        }
    };

    public final static MutationGenerator GAUSSIAN = new MutationGenerator()
    {
        public MutationGenerator()
        {
            super(new GaussianGenerator());
        }

        @Override
        public Matrix.Map next()
        {
            return (double weight) -> weight + getRandomGenerator().next() * getMutationRate();
        }
    };

    private Generator<Double> randomGenerator;
    private double mutationRate;

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

    public void setMutationRate(double mutationRate)
    {
        this.mutationRate = mutationRate;
    }
}