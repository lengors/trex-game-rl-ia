package engine.utils;

public class Utils
{
    public static int max(double... input)
    {
        int index = 0;
        for (int i = 1; i < input.length; ++i)
            index = input[index] < input[i] ? i : index;
        return index;
    }

    public static double[] normalize(double... input)
    {
        Double min = null, max = null;
        for (double value : input)
        {
            if (min == null || value < min)
                min = value;
            if (max == null || value > max)
                max = value;
        }
        double diff = max - min;
        for (int i = 0; i < input.length; ++i)
            input[i] = (input[i] - min) / diff;
        return input;
    }
}