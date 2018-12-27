package tests.trexgame.objects;

import engine.base.GameObject;

import engine.graphics.Window;

import processing.core.PShape;
import processing.core.PImage;
import processing.core.PVector;

public class ShapedObject extends GameObject
{
    private PShape[] texturedMeshs;
    private PVector position;

    public String getName()
    {
        return getClass().getSimpleName();
    }

    @Override
    public void setup()
    {
        Window window = getGame().getResource(Window.class);
        Loader loader = getGame().getResource(Loader.class);
        
        PImage[] textures = loader.get(getName());
        texturedMeshs = new PShape[textures.length];

        for (int i = 0; i < textures.length; ++i)
        {
            PImage texture = textures[i];
            PShape texturedMesh = window.createShape(
                Window.RECT, -texture.width / 2, -texture.height / 2,
                texture.width, texture.height
            );
            texturedMeshs[i] = texturedMesh;
        }
    }

    @Override
    public void update()
    {
        
    }
}