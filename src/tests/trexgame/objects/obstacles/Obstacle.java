package tests.trexgame.objects.obstacles;

import java.util.Random;

import engine.base.GameObject;

import processing.core.PVector;

import java.lang.reflect.InvocationTargetException;

public class Obstacle extends GameObject
{
    private static Class<? extends Obstacle> obstacleClasses[] = new Class[]
    {
        Pterodactyl.class, BigCacti3.class, BigCacti2.class, BigCacti1.class,
        SmallCacti3.class, SmallCacti2.class, SmallCacti1.class
    };
    private static Random random = new Random();

    private PVector position;

    public PVector getPosition()
    {
        return position;
    }

    public static Obstacle random()
    {
        random.setSeed(System.nanoTime());
        int value = random.nextInt(obstacleClasses.length);
        try
        {
            return obstacleClasses[value].getConstructor().newInstance();
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }
}