package tests.trexgame.objects;

import engine.base.Observer;
import engine.base.Observable;

import processing.core.PVector;

public class TrexObject extends BaseObject implements Observer
{
    private Action action = (TrexObject trex) -> { };
    private int offset = 0;

    public TrexObject()
    {
        position = new PVector();
    }

    @Override
    public int getAnimationLength()
    {
        return 2;
    }

    @Override
    public int getAnimationOffset()
    {
        return offset;
    }

    @Override
    public String[] getNames()
    {
        return new String[]
        {
            "dino-2", "dino-3", "dino-down-0", "dino-down-1"
        };
    }

    @Override
    public void setup()
    {
        super.setup();
        position.x = 30;
    }

    @Override
    public void update()
    {
        action.apply(this);
        if (acceleration.y > 0 && position.y >= getGroundPosition())
            offset = 2;
        else
            offset = 0;
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
        if (trex.position.y >= trex.getGroundPosition())
            trex.acceleration.sub(0, 10);
    }

    public static interface Action
    {
        public void apply(TrexObject trex);
    }
}