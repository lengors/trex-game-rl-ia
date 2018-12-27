package tests.trexgame.objects;

import engine.base.Observer;
import engine.base.Observable;

import processing.core.PVector;

public class TrexObject extends BaseObject implements Observer
{
    private Action action = (TrexObject trex) -> { };

    public TrexObject()
    {
        position = new PVector();
    }

    @Override
    public String[] getNames()
    {
        return new String[]
        {
            "dino-2", "dino-3"
        };
    }

    @Override
    public void setup()
    {
        super.setup();
        position = new PVector(25, getGame().getGameObjects(Ground.class).get(0).position.y - 10);
    }

    @Override
    public void update()
    {
        action.apply(this);
        super.update();
    }

    @Override
    public void onChange(Observable observable)
    {
        action = (Action) observable.get();
    }

    @Override
    public void onRegister(Observable observable)
    {
        
    }

    @Override
    public void onUnregister(Observable observable)
    {
        
    }

    public static void down(TrexObject trex)
    {
        trex.acceleration.add(0, 10);
    }

    public static void jump(TrexObject trex)
    {
        if (trex.position.y == trex.getGame().getGameObjects(Ground.class).get(0).position.y - 10)
            trex.acceleration.sub(0, 10);
    }

    public static interface Action
    {
        public void apply(TrexObject trex);
    }
}