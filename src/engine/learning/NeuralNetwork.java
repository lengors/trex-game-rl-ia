package engine.learning;

import engine.math.Matrix;

import engine.learning.genetic.GeneticInformation;

public class NeuralNetwork implements GeneticInformation
{
    private Matrix.Map activationFunction;
    private Matrix[] weights;
    
    public NeuralNetwork(Matrix.Map activationFunction, int... layers)
    {
        this.activationFunction = activationFunction;
        // TODO: randomly initialize weights
    }
    
    public NeuralNetwork(int... layers)
    {
        this((double weight) -> 1. / (1. + Math.exp(-weight)), layers);
    }

    @Override
    public double[][] get(double[][] input)
    {
        Matrix output = Matrix.from(input).addRow(1);
        for (Matrix matrix : weights)
            output.dot(matrix).map(activationFunction).addRow(1);
        return output.toArray();
    }

    @Override
    public double[] get(double[] input)
    {
        Matrix output = Matrix.from(input);
        for (Matrix matrix : weights)
            output.dot(matrix).map(activationFunction).addRow(1);
        return output.toArray(new double[output.size()]);
    }

    public Matrix[] get()
    {
        return weights;
    }
}