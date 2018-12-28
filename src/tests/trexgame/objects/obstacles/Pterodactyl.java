package tests.trexgame.objects.obstacles;

import tests.trexgame.objects.Ground;

import java.util.Random;

public class Pterodactyl extends Obstacle
{
    private static int[] positions = new int[]
    {
        10, 40, 60
    };
    private static Random random = new Random();

    private int pAnimationIndex = 0;

    @Override
    public int getAnimationsPerSec()
    {
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
        random.setSeed(System.nanoTime());
        position.y -= positions[random.nextInt(positions.length)];
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
        position.x -= 1;
    }
}