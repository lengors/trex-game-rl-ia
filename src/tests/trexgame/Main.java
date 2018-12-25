package tests.trexgame;

import java.awt.event.KeyEvent;

import engine.ecs.Observer;
import engine.ecs.Observable;

import engine.graphics.Window;

public class Main extends Window
{
    private Observable observable;
    private Renderer renderer;
    private Trex player;
    private Game game;

    @Override
    public void setup()
    {
        smooth();

        // game = Game.build(TrexGame.class);
        // renderer = game.getRenderer();
        // player = game.make();
    }

    @Override
    public void draw()
    {
        // renderer.render(this);
    }

    @Override
    public void keyPressed()
    {
        for (Observer observer : observable.getObservers())
            observer.onChange(observable);
    }

    public static void main(String[] args)
    {
        Window window = Window.build(Main.class, 400, 400);
    }    
}