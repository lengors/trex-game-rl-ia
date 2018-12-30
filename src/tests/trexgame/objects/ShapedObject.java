package tests.trexgame.objects;

import tests.trexgame.Loader;

import java.util.List;
import java.util.ArrayList;

import engine.base.GameObject;

import engine.graphics.Window;

import processing.core.PShape;
import processing.core.PImage;
import processing.core.PVector;

public class ShapedObject extends GameObject
{
    protected List<PVector>[] touchables;
    protected List<PVector> touchable;
    protected PShape[] texturedMeshs;
    protected PShape texturedMesh;
    protected PVector position;
    protected double animation;
    private PImage[] textures;
    private PImage texture;

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

    public int getHeight()
    {
        return texture.height;
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
        return texturedMesh;
    }

    public PImage getTexture()
    {
        return texture;
    }

    public List<PVector> getTouchable()
    {
        return new ArrayList<>(touchable);
    }

    public int getWidth()
    {
        return texture.width;
    }

    @Override
    public void setup()
    {
        Window window = getGame().getResource(Window.class);
        Loader loader = getGame().getResource(Loader.class);
        
        textures = loader.get(getNames());
        touchables = new List[textures.length];
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

            texture.loadPixels();
            List<PVector> touchable = new ArrayList<>();
            for (int j = 0; j < texture.height; ++j)
            {
                int y = j * texture.width;
                for (int x = 0; x < texture.width; ++x)
                {
                    int color = texture.pixels[y + x];
                    if (window.red(color) == 83 && window.green(color) == 83 && window.blue(color) == 83)
                        touchable.add(new PVector(x, j));
                }
            }
            touchables[i] = touchable;
        }
        
        int animationIndex = getAnimationIndex();
        texturedMesh = texturedMeshs[animationIndex];
        touchable = touchables[animationIndex];
        texture = textures[animationIndex];
    }

    @Override
    public void update()
    {
        animation += getAnimationsPerSec() * getGame().getUpsNS() * 1e-9;
        if (animation >= getAnimationLength())
            animation = 0;
        int animationIndex = getAnimationIndex();
        texturedMesh = texturedMeshs[animationIndex];
        touchable = touchables[animationIndex];
        texture = textures[animationIndex];
    }
}