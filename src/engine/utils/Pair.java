package engine.utils;

public class Pair<T, U>
{
    private T key;
    private U value;

    public Pair(T key, U value)
    {
        this.key = key;
        this.value = value;
    }

    public Pair()
    {
        key = null;
        value = null;
    }

    public T getKey()
    {
        return key;
    }

    public Pair<T, U> setKey(T key)
    {
        this.key = key;
        return this;
    }

    public U getValue()
    {
        return value;
    }

    public Pair<T, U> setValue(U value)
    {
        this.value = value;
        return this;
    }

    @Override
    public String toString()
    {
        return key + ": " + value;
    }
}