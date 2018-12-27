package tests.trexgame;

import engine.graphics.Window;

import java.util.Map;
import java.util.HashMap;

import processing.core.PImage;

public class Loader
{
    private Map<String, PImage> textures = new HashMap<>();
    private Window window;

    public Loader(Window window)
    {
        this.window = window;
    }

    public PImage[] get(String... names)
    {
        PImage[] ts = new PImage[names.length];
        for (int i = 0; i < names.length; ++i)
        {
            String name = names[i];
            PImage texture = textures.get(name);
            if (texture == null)
                texture = load(name);
            ts[i] = texture;
        }
        return ts;
    }

    public PImage load(String name)
    {
        PImage texture = window.loadImage("res/textures/" + name + ".png");
        textures.put(name, texture);
        return texture;
    }
}