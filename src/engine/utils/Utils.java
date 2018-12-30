package engine.utils;

public class Utils
{
    public static int max(double... input)
    {
        int index = 0;
        for (int i = 1; i < input.length; ++i)
            index = input[index] > input[i] ? i : index;
        return index;
    }
}