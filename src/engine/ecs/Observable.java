package engine.ecs;

import java.util.Set;
import java.util.TreeSet;

public class Observable
{
    private Set<Observer> observers = new TreeSet<>();

    public Set<Observer> getObservers()
    {
        return observers;
    }

    public Observable register(Observer observer)
    {
        observers.add(observer);
        observer.onRegister(this);
        return this;
    }

    public Observable unregister(Observer observer)
    {
        observers.remove(observer);
        observer.onUnregister(this);
        return this;
    }
}