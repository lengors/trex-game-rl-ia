package tests.trexgame.states;

import engine.GameState;

import engine.ecs.Observer;
import engine.ecs.Observable;

public class MainState extends GameState implements Observer
{
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

    }

    @Override
    public void onExit()
    {

    }

    @Override
    public void onChange(Observable observable)
    {
        // if (observable.getChange() == TrexState.JUMP)
        // 
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
        
    }
}