package engine.utils;

public class Synced<T>
{
    protected T value;

    public Synced(T value)
    {
        this.value = value;
    }

    public T getValue()
    {
        return value;
    }
}