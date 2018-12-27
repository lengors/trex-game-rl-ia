package tests.trexgame.objects;

import processing.core.PVector;

public class BaseObject extends ShapedObject
{
    protected PVector acceleration;
    protected PVector velocity;

    @Override
    public void setup()
    {
        super.setup();
        velocity = new PVector();
        acceleration = new PVector();
        position = new PVector(0, getGroundPosition());
    }

    @Override
    public void update()
    {
        super.update();
        acceleration.add(0, .7f);
        velocity.add(acceleration);
        position.add(velocity);
        acceleration.set(0, 0);
        float ground = getGroundPosition();
        if (position.y > ground)
        {
            acceleration.sub(0, .7f);
            position.y = ground;
            velocity.y = 0;
        }
    }
}