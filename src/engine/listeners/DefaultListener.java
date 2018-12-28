package engine.listeners;

import java.util.List;
import java.util.ArrayList;

import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class DefaultListener implements InputListener
{
    private List<MouseListener> mouseListeners = new ArrayList<>();
    private List<KeyListener> keyListeners = new ArrayList<>();

    @Override
    public void onKeyPress(KeyEvent event)
    {
        for (KeyListener listener : keyListeners)
            listener.onKeyPress(event);
    }
    
    @Override
    public void onKeyRelease(KeyEvent event)
    {
        for (KeyListener listener : keyListeners)
            listener.onKeyRelease(event);
    }

    @Override
    public void onMouseClick(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMouseClick(event);
    }
    
    @Override
    public void onMouseDrag(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMouseDrag(event);
    }

    @Override
    public void onMouseMove(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMouseMove(event);
    }
    
    @Override
    public void onMousePress(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMousePress(event);
    }

    @Override
    public void onMouseRelease(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMouseRelease(event);
    }

    @Override
    public void onMouseWheel(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMouseWheel(event);
    }

    public DefaultListener addListener(InputListener inputListener)
    {
        mouseListeners.add(inputListener);
        keyListeners.add(inputListener);
        return this;
    }

    public DefaultListener addListener(MouseListener mouseListener)
    {
        mouseListeners.add(mouseListener);
        return this;
    }

    public DefaultListener addListener(KeyListener keyListener)
    {
        keyListeners.add(keyListener);
        return this;
    }

    public DefaultListener removeListener(InputListener inputListener)
    {
        mouseListeners.remove(inputListener);
        keyListeners.remove(inputListener);
        return this;
    }

    public DefaultListener removeListener(MouseListener mouseListener)
    {
        mouseListeners.remove(mouseListener);
        return this;
    }

    public DefaultListener removeListener(KeyListener keyListener)
    {
        keyListeners.remove(keyListener);
        return this;
    }
}