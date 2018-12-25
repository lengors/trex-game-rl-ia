package tests;

import processing.core.PImage;
import processing.core.PVector;

import processing.event.KeyEvent;
import processing.event.MouseEvent;

import engine.graphics.Window;

import java.util.List;
import java.util.ArrayList;

public class Main extends Window
{
    private PVector init, end;
    private PImage image;
    private float scale;
    private int x, y;

    public PImage makeFigure(PVector initialPosition)
    {
        List<PVector> positions = new ArrayList<>();
        List<PVector> openedPositions = new ArrayList<>();
        PVector[] directions = new PVector[]
        {
            new PVector(1, 0), new PVector(-1, 0), new PVector(0, 1), new PVector(0, -1)
        };
        openedPositions.add(initialPosition);
        while (openedPositions.size() > 0)
        {
            PVector vector = openedPositions.remove(0);
            int x = (int) vector.x;
            int y = (int) vector.y;
            if (x >= 0 && y >= 0 && x < image.width && y < image.height)
            {
                int c = image.get(x, y);
                if (c != color(64, 202, 201))
                {
                    positions.add(vector);
                    for (PVector direction : directions)
                        openedPositions.add(PVector.add(vector, direction));
                }
            }
        }
        return null;
    }

    @Override
    public void setup()
    {
        smooth();
        scale = 1;
        image = loadImage("res/textures/sprite.png");
    }

    @Override
    public void draw()
    {
        background(255, 0, 0);
        image(image, x, y, image.width * scale, image.height * scale);
        if (init != null && end != null)
        {
            noFill();
            stroke(0);
            rect(init.x, init.y, end.x - init.x, end.y - init.y);
        }
    }

    @Override
    public void keyPressed(KeyEvent event)
    {
        if (event.isControlDown())
        {
            if (event.getKeyCode() == 521)
                scale += scale * .1f;
            else if (event.getKeyCode() == 45)
                scale -= scale * .1f;
        }
    }

    @Override
    public void mousePressed(MouseEvent event)
    {
        if (event.isShiftDown())
            end = init = new PVector(mouseX, mouseY);
        else
        {
            loadPixels();
            int color = get(mouseX, mouseY);
            System.out.printf("color: (%03d, %03d, %03d)\n", (int) red(color), (int) green(color), (int) blue(color));
        }
    }

    @Override
    public void mouseDragged(MouseEvent event)
    {
        if (event.isShiftDown())
            end = new PVector(mouseX, mouseY);
        else
        {
            float dx = mouseX - pmouseX;
            float dy = mouseY - pmouseY;
            x += dx;
            y += dy;
            if (init != null && end != null)
            {
                end.add(dx, dy); 
                init.add(dx, dy);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent event)
    {
        if (event.isShiftDown())
            end = new PVector(mouseX, mouseY);
    }

    public static void main(String[] args)
    {
        Window window = Window.build(Main.class, 400, 400);
    }
}