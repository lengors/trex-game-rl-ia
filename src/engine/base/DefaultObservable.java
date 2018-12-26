package engine.base;

import java.util.List;
import java.util.ArrayList;

public class DefaultObservable implements Observable
{
    private List<Observer> observers = new ArrayList<>();
    private Observable observable;
    private Object object;

    public DefaultObservable(Observable observable)
    {
        this.observable = observable;
    }

    public DefaultObservable()
    {
        this.observable = this;
    }

    public Observable addObserver(Observer observer)
    {
        observers.add(observer);
        return observable;
    }

    public Observable dispatch(Object object)
    {
        this.object = object;
        for (Observer observer : observers)
            observer.onChange(observable);
        this.object = null;
        return observable;
    }

    @Override
    public Object get()
    {
        return object;
    }

    public Observer getObserver(int index)
    {
        return observers.get(index);
    }

    public int getObserverCount()
    {
        return observers.size();
    }

    public int getObserverIndex(Observer observer)
    {
        return observers.indexOf(observer);
    }

    public List<Observer> getObservers()
    {
        return new ArrayList<>(observers);
    }

    public Observer removeObserver(int index)
    {
        return observers.remove(index);
    }

    public boolean removeObserver(Observer observer)
    {
        return observers.remove(observer);
    }
}