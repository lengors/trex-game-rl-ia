package tests.trexgame;

import java.awt.event.KeyEvent;

import engine.ecs.Observer;
import engine.ecs.Observable;

import engine.graphics.Window;

import java.util.List;
import java.util.ArrayList;

public class Main extends Window
{
    @Override
    public void setup()
    {
        smooth();
    }

    @Override
    public void draw()
    {

    }

    @Override
    public void keyPressed()
    {
        
    }

    public static void main(String[] args)
    {
        Window window = Window.build(Main.class, 400, 400);
    }    
}