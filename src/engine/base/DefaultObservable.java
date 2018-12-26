package engine.base;

public class DefaultObservable implements Observable
{
    private Observable observable;

    public DefaultObservable(Observable observable)
    {
        this.observable = observable;
    }

    public DefaultObservable()
    {
        this(this);
    }
}