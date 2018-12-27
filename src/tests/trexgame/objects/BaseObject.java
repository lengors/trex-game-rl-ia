package tests.trexgame.objects;

import processing.core.PVector;

public class BaseObject extends ShapedObject
{
    protected PVector acceleration;
    protected PVector velocity;
    // private long lastTime;

    @Override
    public void setup()
    {
        super.setup();
        velocity = new PVector();
        acceleration = new PVector();
        // lastTime = System.nanoTime();
    }

    @Override
    public void update()
    {
        // long now = System.nanoTime();
        // float delta = 
        super.update();
        acceleration.add(0, .7f);
        velocity.add(acceleration);
        position.add(velocity);
        acceleration.set(0, 0);
        float ground = getGame().getGameObjects(Ground.class).get(0).position.y - 10;
        if (position.y > ground)
        {
            position.y = ground;
            velocity.y = 0;
        }
    }
}