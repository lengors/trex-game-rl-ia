package engine.math;

public class Matrix
{
    private int width, height;
    private double[] data;

    public Matrix(int height, int width)
    {
        data = new double[width * height];
        this.height = height;
        this.width = width;
    }

    public Matrix add(Matrix matrix)
    {
        for (int i = 0; i < data.length; ++i)
            data[i] += matrix.data[i];
        return this;
    }

    public Matrix add(double scalar)
    {
        for (int i = 0; i < data.length; )
            data[i++] += scalar;
        return this;
    }

    public Matrix addColumn(int initializer)
    {
        // TODO: add column initilized with initializer
        return null;
    }

    public Matrix addRow(int initializer)
    {
        // TODO: add row initilized with initializer
        return null;
    }

    public Matrix copy()
    {
        Matrix matrix = new Matrix(height, width);
        System.arraycopy(data, 0, matrix.data, 0, data.length);
        return matrix;
    }

    public Matrix div(Matrix matrix)
    {
        for (int i = 0; i < data.length; ++i)
            data[i] /= matrix.data[i];
        return this;
    }

    public Matrix div(double scalar)
    {
        for (int i = 0; i < data.length; )
            data[i++] /= scalar;
        return this;
    }

    public Matrix dot(Matrix matrix)
    {
        // TODO: dot product
        return null;
    }

    public Matrix map(Map mapFunction)
    {
        for (int i = 0; i < data.length; ++i)
            data[i] = mapFunction.map(data[i]);
        return this;
    }

    public Matrix mul(Matrix matrix)
    {
        for (int i = 0; i < data.length; ++i)
            data[i] *= matrix.data[i];
        return this;
    }

    public Matrix mul(double scalar)
    {
        for (int i = 0; i < data.length; )
            data[i++] *= scalar;
        return this;
    }

    public int size()
    {
        return data.length;
    }

    public Matrix sub(Matrix matrix)
    {
        for (int i = 0; i < data.length; ++i)
            data[i] -= matrix.data[i];
        return this;
    }

    public Matrix sub(double scalar)
    {
        for (int i = 0; i < data.length; )
            data[i++] -= scalar;
        return this;
    }

    public double[] toArray(double[] array)
    {
        System.arraycopy(data, 0, array, 0, data.length);
        return array;
    }

    public double[][] toArray()
    {
        double[][] array = new double[height][width];
        for (int i = 0; i < data.length; ++i)
            System.arraycopy(data, i * width, array[i], 0, width);
        return array;
    }

    public static Matrix from(double[][] array)
    {
        Matrix matrix = new Matrix(array.length, array[0].length);
        for (int i = 0; i < array.length; ++i)
            System.arraycopy(array[i], 0, matrix.data, matrix.width * i, array[i].length);
        return matrix;
    }

    public static Matrix from(double[] array)
    {
        Matrix matrix = new Matrix(array.length, 1);
        System.arraycopy(array, 0, matrix.data, 0, array.length);
        return matrix;
    }

    public static interface Map
    {
        public double map(double value);
    }
}