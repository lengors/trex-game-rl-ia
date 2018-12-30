package tests.trexgame.objects;

import engine.base.Observer;
import engine.base.Observable;

import processing.core.PImage;
import processing.core.PVector;

import tests.trexgame.objects.obstacles.Obstacle;

import java.util.List;

public class TrexObject extends BaseObject implements Observer
{
    private Action action = (TrexObject trex) -> { };
    private int offset = 0, length = 2;
    private Caller caller;

    public TrexObject(Caller caller)
    {
        this.caller = caller;
    }

    public TrexObject()
    {
    }

    public boolean collides(Obstacle obstacle)
    {
        PImage thisTexture = getTexture();
        PImage obstacleTexture = obstacle.getTexture();
        float thisX = (float) Math.floor(position.x - thisTexture.width / 2);
        float thisY = (float) Math.floor(position.y - thisTexture.height / 2);
        float obstacleX = (float) Math.floor(obstacle.getPosition().x - obstacleTexture.width / 2);
        float obstacleY = (float) Math.floor(obstacle.getPosition().y - obstacleTexture.height / 2);
        if (thisX < obstacleX + obstacleTexture.width && thisX + thisTexture.width > obstacleX && thisY < obstacleY + obstacleTexture.height && thisY + thisTexture.height > obstacleY)
        {
            List<PVector> toTest = obstacle.getTouchable();
            for (int i = 0; i < toTest.size(); ++i)
                toTest.set(i, PVector.add(toTest.get(i), new PVector(obstacleX, obstacleY)));
            for (PVector thisV : getTouchable())
            {
                float x = thisV.x + thisX;
                float y = thisV.y + thisY;
                for (PVector obstacleV : toTest)
                    if (x == obstacleV.x && y == obstacleV.y)
                        return true;
            }
        }
        return false;
    }

    @Override
    public int getAnimationLength()
    {
        return length;
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
            "dino-2", "dino-3", "dino-down-0", "dino-down-1", "dino-0"
        };
    }

    public Obstacle getObstacle()
    {
        List<Obstacle> obstacles = getGame().getGameObjects(Obstacle.class);
        if (obstacles.size() > 0)
            return obstacles.get(0);
        return null;
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
        action = caller.call(this);
        action.apply(this);
        if (acceleration.y > 0 && position.y >= getGroundPosition())
            length = offset = 2;
        else if (position.y < getGroundPosition())
        {
            offset = 4;
            length = 1;
        }
        else
        {
            offset = 0;
            length = 2;
        }
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
            trex.acceleration.sub(0, 11);
    }

    public static interface Action
    {
        public void apply(TrexObject trex);
    }

    public static interface Caller
    {
        public Action call(TrexObject trex);
    }
}