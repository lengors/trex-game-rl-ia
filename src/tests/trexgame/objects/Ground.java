package tests.trexgame.objects;

import processing.core.PVector;

import engine.graphics.Window;

public class Ground extends ShapedObject
{
    private double speed = 6;

    @Override
    public String[] getNames()
    {
        return new String[]
        {
            "ground"
        };
    }

    public double getSpeed()
    {
        return speed;
    }

    @Override
    public void setup()
    {
        super.setup();
        Window window = getGame().getResource(Window.class);
        position = new PVector(getTexture().width / 2, 300);
    }

    @Override
    public void update()
    {
        position.x -= speed;
        position.x %= getTexture().width;
    }
}