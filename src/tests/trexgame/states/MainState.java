package tests.trexgame.states;

import java.util.List;

import engine.base.Game;
import engine.base.GameState;

import engine.ecs.Observer;
import engine.ecs.Observable;

import tests.trexgame.objects.obstacles.Obstacle;

public class MainState extends GameState implements Observer
{
    private double gameSpeed;

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
        gameSpeed = 6;
    }

    @Override
    public void onExit()
    {

    }

    @Override
    public void onChange(Observable observable)
    {

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
        List<Obstacle> obstacles = getGame().getObjects(Obstacle.class);
        while (obstacles.size() < 2)
            getGame().addObject(Obstacle.random());
        
        
        return getName();
    }
}