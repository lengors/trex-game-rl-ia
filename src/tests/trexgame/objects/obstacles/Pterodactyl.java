package tests.trexgame.objects.obstacles;

import processing.core.PVector;

import tests.trexgame.objects.Ground;
import engine.base.Observable;
import engine.base.Observer;

public class Pterodactyl extends Obstacle
{
    private int pAnimationIndex = 0;

    @Override
    public int getAnimationsPerSec() {
        return 4;
    }

    @Override
    public int getGroundPosition()
    {
        return (int) getGame().getGameObjects(Ground.class).get(0).getPosition().y;
    }

    @Override
    public String[] getNames()
    {
        return new String[]
        {
            "pet-0", "pet-1"
        };
    }

    @Override
    public void setup()
    {
        super.setup();
        position.y = getGroundPosition();
    }

    @Override
    public void update()
    {
        super.update();
        int cIndex = getAnimationIndex();
        if (pAnimationIndex != cIndex)
        {
            if (cIndex == 0)
                position.y += 8;
            else
                position.y -= 8;
            pAnimationIndex = cIndex;
        }
    }
}