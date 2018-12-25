package tests.trexgame.states;

import engine.base.GameState;

import engine.ecs.Observer;
import engine.ecs.Observable;

public class LostState extends GameState implements Observer
{
    private String toReturn;

    @Override
    public void onUnregister()
    {

    }

    @Override
    public void onRegister()
    {

    }

    @Override
    public void onEnter()
    {
        toReturn = getName();
    }

    @Override
    public void onExit()
    {

    }

    @Override
    public void onChange(Observable observable)
    {
        // observable.getChange();
    }

    @Override
    public void onRegister(Observable observable)
    {
        
    }

    @Override
    public void onUnregister(Observable observable)
    {
        
    }

    @Override
    public String onRun()
    {
        return toReturn;
    }
}