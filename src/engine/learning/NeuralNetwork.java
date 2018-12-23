package engine.learning;

import engine.math.Matrix;

import engine.learning.genetic.GeneticInformation;

public class NeuralNetwork implements GeneticInformation
{
    private Matrix[] weights;

    @Override
    public double[][] get(double[][] input)
    {
        Matrix output = Matrix.from(input).addRow(1, 1);
        for (Matrix matrix : weights)
            output.mul(matrix).addRow(1, 1);
        return output.toBidimensionalArray();
    }

    @Override
    public double[] get(double[] input)
    {
        Matrix output = Matrix.from(input);
        for (Matrix matrix : weights)
            output.mul(matrix).addRow(1, 1);
        return output.toArray();
    }

    public Matrix[] get()
    {
        return weights;
    }
}