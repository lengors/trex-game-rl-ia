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
    private PImage[] textures;

    public int getAnimationIndex()
    {
        int index = (int) Math.floor(animation);
        index %= getAnimationLength();
        return index + getAnimationOffset();
    }

    public int getAnimationLength()
    {
        return texturedMeshs.length;
    }

    public int getAnimationOffset()
    {
        return 0;
    }

    public int getAnimationsPerSec()
    {
        return 10;
    }

    public int getGroundPosition()
    {
        int groundPosition = (int) getGame().getGameObjects(Ground.class).get(0).position.y;
        return groundPosition - (int) (getTexture().height / 2) + 5;
    }

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
        return texturedMeshs[getAnimationIndex()];
    }

    public PImage getTexture()
    {
        return textures[getAnimationIndex()];
    }

    @Override
    public void setup()
    {
        Window window = getGame().getResource(Window.class);
        Loader loader = getGame().getResource(Loader.class);
        
        textures = loader.get(getNames());
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
        animation += getAnimationsPerSec() * getGame().getUpsNS() * 1e-9;
        if (animation >= getAnimationLength())
            animation = 0;
    }
}