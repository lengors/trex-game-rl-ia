package tests.trexgame.objects.obstacles;

import java.util.List;
import java.util.Random;

import java.lang.reflect.InvocationTargetException;

import engine.graphics.Window;

import tests.trexgame.objects.Ground;
import tests.trexgame.objects.ShapedObject;

import processing.core.PVector;

public class Obstacle extends ShapedObject
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
        random.setSeed(System.nanoTime());
        Window window = getGame().getResource(Window.class);
        List<Obstacle> list = getGame().getGameObjects(Obstacle.class);
        if (list.size() == 0 || list.get(list.size() - 1) == this)
            position = new PVector(window.width * 2 + random.nextInt(window.width), getGroundPosition());
        else
            position = new PVector(list.get(list.size() - 1).getPosition().x + window.width / 2 + random.nextInt(window.width / 2 - getTexture().width) + getTexture().width, getGroundPosition());
    }

    @Override
    public void update()
    {
        super.update();
        position.x -= getGame().getGameObjects(Ground.class).get(0).getSpeed();
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