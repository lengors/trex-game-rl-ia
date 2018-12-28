package tests.trexgame.objects;

import processing.core.PVector;

import engine.graphics.Window;

public class Ground extends ShapedObject
{
    private double inScore = 0;
    private double speed = 6;
    private int score = 0;

    @Override
    public String[] getNames()
    {
        return new String[]
        {
            "ground"
        };
    }

    public int getScore()
    {
        return score;
    }

    public double getSpeed()
    {
        return speed;
    }

    @Override
    public void setup()
    {
        super.setup();
        position = new PVector(getTexture().width / 2, 300);
    }

    @Override
    public void update()
    {
        inScore += (speed + 5) * getGame().getUpsNS() * 1e-9;
        score = (int) Math.floor(inScore);
        position.x -= speed;
        position.x %= getTexture().width;
        speed += 0.002;
    }
}