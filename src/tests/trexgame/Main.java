package tests.trexgame;

import engine.graphics.Window;

public class Main extends Window
{
    private List<MeshObject> meshObjects;

    @Override
    public void setup()
    {
        smooth();
        Observer.register(meshObjects, gameObjects);
    }

    @Override
    public void draw()
    {
        background(255);
    }

    public static main(String[] args)
    {
        Window window = Window.build(Main.class);
    }    
}