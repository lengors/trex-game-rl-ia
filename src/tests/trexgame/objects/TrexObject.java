package tests.trexgame.objects;

import engine.base.Observer;
import engine.base.Observable;

import processing.core.PVector;

public class TrexObject extends ShapedObject implements Observer
{
    public TrexObject()
    {
        position = new PVector();
    }

    public String[] getNames()
    {
        return new String[]
        {
            "dino-2", "dino-3"
        };
    }

    @Override
    public void onChange(Observable observable)
    {
        position = (PVector) observable.get();
    }

    @Override
    public void onRegister(Observable observable)
    {
        
    }

    @Override
    public void onUnregister(Observable observable)
    {
        
    }
}