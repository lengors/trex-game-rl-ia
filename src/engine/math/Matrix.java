package engine.math;

import java.util.Random;

public class Matrix
{
    private static Random random = new Random();

    public static final Map RANDOMIZE = (double weight) ->
    {
        random.setSeed(System.nanoTime());
        return random.nextDouble();
    };

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
        double[] data = new double[this.data.length + height];
        for (int i = 0; i < height; ++i)
        {
            int position = i * width;
            int newPosition = position + i;
            data[newPosition++] = initializer;
            System.arraycopy(this.data, position, data, newPosition, width);
        }
        this.data = data;
        width += 1;
        return this;
    }

    public Matrix addRow(int initializer)
    {
        double[] data = new double[this.data.length + width];
        for (int i = 0; i < width; )
            data[i++] = initializer;
        System.arraycopy(this.data, 0, data, width, this.data.length);
        this.data = data;
        height += 1;
        return this;
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
        double[] newData = new double[height * matrix.width];
        for (int i = 0; i < height; ++i)
        {
            int position = i * width;
            int newPosition = i * matrix.width;
            for (int j = 0; j < matrix.width; ++j)
            {
                double sum = 0;
                for (int k = 0; k < width; ++k)
                    sum += data[position + k] * matrix.data[k * matrix.width + j];
                newData[newPosition + j] = sum;
            }
        }
        width = matrix.width;
        this.data = newData;
        return this;
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

    @Override
    public String toString()
    {
        String string = "";
        for (int i = 0; i < height; )
        {
            int position = i++ * width;
            for (int j = 0; j < width; )
            {
                string += data[position + j++];
                string += j == width ? "" : " ";
            }
            string += i == height ? "" : "\n";
        }
        return string;
    }

    public static Matrix dot(Matrix a, Matrix b)
    {
        Matrix matrix = new Matrix(a.height, b.width);
        for (int i = 0; i < a.height; ++i)
        {
            int position = i * a.width;
            int newPosition = i * b.width;
            for (int j = 0; j < b.width; ++j)
            {
                double sum = 0;
                for (int k = 0; k < a.width; ++k)
                    sum += a.data[position + k] * b.data[k * b.width + j];
                matrix.data[newPosition + j] = sum;
            }
        }
        return matrix;
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