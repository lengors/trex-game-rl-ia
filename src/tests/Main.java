package tests;

import engine.graphics.Window;

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
        background(0, 255, 0);
    }

    public static void main(String[] args)
    {
        Window window = Window.build(Main.class, 400, 400);
    }
}