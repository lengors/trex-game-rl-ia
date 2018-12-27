package tests.trexgame.objects;

import processing.core.PVector;

public class Ground extends ShapedObject
{
    @Override
    public String[] getNames()
    {
        return new String[]
        {
            "ground"
        };
    }

    @Override
    public void setup()
    {
        super.setup();
        position = new PVector(0, 300);
    }
}