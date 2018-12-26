package engine.listeners;

import processing.event.MouseEvent;

public interface MouseListener
{
    public void onMouseMove(MouseEvent event);
    public void onMouseDrag(MouseEvent event);
    public void onMousePress(MouseEvent event);
    public void onMouseWheel(MouseEvent event);
    public void onMouseRelease(MouseEvent event);
}