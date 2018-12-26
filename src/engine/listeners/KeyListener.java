package engine.listeners;

import processing.event.KeyEvent;

public interface KeyListener
{
    public void onKeyPress(KeyEvent event);
    public void onKeyRelease(KeyEvent event);
}