package engine.ecs;

public abstract class Observer
{
    public abstract void onChange(Observable observable);
    public abstract void onRegister(Observable observable);
    public abstract void onUnregister(Observable observable);

    public Observer observe(Observable observable)
    {
        observable.register(this);
        return this;
    }

    public Observer unobserve(Observable observable)
    {
        observable.unregister(this);
        return this;
    }
}