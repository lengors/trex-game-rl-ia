package engine.utils;

import java.util.Random;

public class GaussianGenerator implements Generator<Double>
{
    private Random random = new Random();
    private double mean, deviation;

    public GaussianGenerator(double mean, double deviation)
    {
        this.deviation = deviation;
        this.mean = mean;
    }

    public GaussianGenerator()
    {
        this(0, 1);
    }

    @Override
    public Double next()
    {
        random.setSeed(System.nanoTime());
        return random.nextGaussian() * deviation + mean;
    }
}