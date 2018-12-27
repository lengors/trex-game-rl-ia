package tests.trexgame.objects;

import engine.base.GameObject;

import engine.graphics.Window;

import processing.core.PVector;
import processing.core.PShape;

public class ShapedObject extends GameObject
{
    private PVector position;
    private PShape mesh;

    @Override
    public void setup()
    {
        Window window = getGame().getResource(Window.class);
    }

    @Override
    public void update()
    {
        
    }
}