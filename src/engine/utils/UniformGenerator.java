package engine.utils;

import java.util.Random;

public class UniformGenerator implements Generator<Double>
{
    private Random random = new Random();
    private double interval, minimal;
    
    public UniformGenerator(double min, double max)
    {
        interval = max - min;
        minimal = min;
    }

    public UniformGenerator()
    {
        this(0, 1);
    }

    @Override
    public Double next()
    {
        random.setSeed(System.nanoTime());
        return random.nextDouble() * interval - minimal;
    }
}