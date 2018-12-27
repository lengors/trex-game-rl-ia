package tests.trexgame.objects.obstacles;

import java.util.Random;

import java.lang.reflect.InvocationTargetException;

import tests.trexgame.objects.Ground;
import tests.trexgame.objects.BaseObject;

import processing.core.PVector;

public class Obstacle extends BaseObject
{
    private static Class<? extends Obstacle>[] obstaclesClasses = new Class[]
    {
        Pterodactyl.class,
        BigCacti1.class, BigCacti2.class, BigCacti3.class,
        SmallCacti1.class, SmallCacti2.class, SmallCacti3.class
    };
    private static Random random = new Random();

    @Override
    public void setup()
    {
        super.setup();
        position.x = 350;
    }

    public static Obstacle getRandomObstacle()
    {
        random.setSeed(System.nanoTime());
        int index = random.nextInt(obstaclesClasses.length);
        try
        {
            return obstaclesClasses[index].getConstructor().newInstance();
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }
}