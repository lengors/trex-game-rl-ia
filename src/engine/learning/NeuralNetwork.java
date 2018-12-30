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
        weights = new Matrix[layers.length - 1];
        for (int i = 0; i < weights.length; ++i)
            weights[i] = new Matrix(layers[i + 1], layers[i] + 1);
    }
    
    public NeuralNetwork(int... layers)
    {
        this((double weight) -> 1. / (1. + Math.exp(-weight)), layers);
    }

    @Override
    public double[][] get(double[][] input)
    {
        return get(Matrix.from(input)).toArray();
    }

    @Override
    public double[] get(double... input)
    {
        Matrix output = get(Matrix.from(input));
        return output.toArray(new double[output.size()]);
    }

    public Matrix get(Matrix output)
    {
        output.addRow(1);
        for (Matrix matrix : weights)
            output = Matrix.dot(matrix, output).map(activationFunction).addRow(1);
        return output;
    }

    public Matrix get(int i)
    {
        return weights[i];
    }

    public Matrix[] get()
    {
        return weights;
    }

    public int[] layers()
    {
        int[] layers = new int[weights.length + 1];
        layers[0] = weights[0].getWidth() - 1;
        for (int i = 1; i < weights.length; ++i)
            layers[i] = weights[i].getWidth() - 1;
        layers[weights.length] = weights[weights.length - 1].getHeight();
        return layers;
    }

    public NeuralNetwork map(Matrix.Map map)
    {
        for (Matrix matrix : weights)
            matrix.map(map);
        return this;
    }

    public int size()
    {
        return weights.length;
    }

    @Override
    public String toString()
    {
        String string = "";
        for (int i = 0; i < weights.length; ++i)
            string += weights[i] + (i == weights.length - 1 ? "\n" : "");
        return string;
    }
}