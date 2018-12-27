package tests.trexgame.objects;

import tests.trexgame.Loader;

import engine.base.GameObject;

import engine.graphics.Window;

import processing.core.PShape;
import processing.core.PImage;
import processing.core.PVector;

public class ShapedObject extends GameObject
{
    protected PShape[] texturedMeshs;
    protected PVector position;
    protected double animation;

    public String[] getNames()
    {
        return new String[]
        {
            getClass().getSimpleName()
        };
    }

    public PVector getPosition()
    {
        return position;
    }

    public PShape getShape()
    {
        int index = (int) Math.floor(animation);
        return texturedMeshs[index % texturedMeshs.length];
    }

    @Override
    public void setup()
    {
        Window window = getGame().getResource(Window.class);
        Loader loader = getGame().getResource(Loader.class);
        
        PImage[] textures = loader.get(getNames());
        texturedMeshs = new PShape[textures.length];

        for (int i = 0; i < textures.length; ++i)
        {
            PImage texture = textures[i];
            PShape texturedMesh = window.createShape(
                Window.RECT, -texture.width / 2, -texture.height / 2,
                texture.width, texture.height
            );
            texturedMesh.setTexture(texture);
            texturedMeshs[i] = texturedMesh;
        }
    }

    @Override
    public void update()
    {
        animation += getGame().getUpsNS() * 1e-8;
        if (animation >= texturedMeshs.length)
            animation = 0;
    }
}