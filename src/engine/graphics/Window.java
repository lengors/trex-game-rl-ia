package engine.graphics;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import processing.core.PApplet;

import processing.event.KeyEvent;
import processing.event.MouseEvent;

import engine.listeners.KeyListener;
import engine.listeners.MouseListener;
import engine.listeners.InputListener;

public class Window extends PApplet implements InputListener
{
    private List<MouseListener> mouseListeners = new ArrayList<>();
    private List<KeyListener> keyListeners = new ArrayList<>();
    private Builder builder;

    @Override
    public void settings()
    {
        if (builder.is(Integer.class, "width", "height"))
        {
            int width = builder.<Integer>get("width");
            int height = builder.<Integer>get("height");
            if (builder.is(String.class, "renderer"))
                size(width, height, builder.<String>get("renderer"));
            else
                size(width, height);
        }
        else if (builder.is(String.class, "renderer"))
            fullScreen(builder.<String>get("renderer"));
        else
            fullScreen();
    }

    @Override
    public void keyPressed(KeyEvent event)
    {
        for (KeyListener listener : keyListeners)
            listener.onKeyPress(event);
    }
    
    @Override
    public void keyReleased(KeyEvent event)
    {
        for (KeyListener listener : keyListeners)
            listener.onKeyRelease(event);
    }

    @Override
    public void mouseClicked(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMouseClick(event);
    }
    
    @Override
    public void mouseDragged(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMouseDrag(event);
    }

    @Override
    public void mouseMoved(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMouseMove(event);
    }
    
    @Override
    public void mousePressed(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMousePress(event);
    }

    @Override
    public void mouseReleased(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMouseRelease(event);
    }

    @Override
    public void mouseWheel(MouseEvent event)
    {
        for (MouseListener listener : mouseListeners)
            listener.onMouseWheel(event);
    }

    @Override
    public void onKeyPress(KeyEvent event)
    {
        if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE)
            exit();
    }
    
    @Override
    public void onKeyRelease(KeyEvent event)
    {

    }

    @Override
    public void onMouseClick(MouseEvent event)
    {

    }
    
    @Override
    public void onMouseDrag(MouseEvent event)
    {

    }

    @Override
    public void onMouseMove(MouseEvent event)
    {

    }
    
    @Override
    public void onMousePress(MouseEvent event)
    {

    }

    @Override
    public void onMouseRelease(MouseEvent event)
    {

    }

    @Override
    public void onMouseWheel(MouseEvent event)
    {

    }

    public Window addListener(InputListener inputListener)
    {
        mouseListeners.add(inputListener);
        keyListeners.add(inputListener);
        return this;
    }

    public Window addListener(MouseListener mouseListener)
    {
        mouseListeners.add(mouseListener);
        return this;
    }

    public Window addListener(KeyListener keyListener)
    {
        keyListeners.add(keyListener);
        return this;
    }

    public Window setBuilder(Builder builder)
    {
        this.builder = builder;
        return this;
    }
    
    public Window removeListener(InputListener inputListener)
    {
        mouseListeners.remove(inputListener);
        keyListeners.remove(inputListener);
        return this;
    }

    public Window removeListener(MouseListener mouseListener)
    {
        mouseListeners.remove(mouseListener);
        return this;
    }

    public Window removeListener(KeyListener keyListener)
    {
        keyListeners.remove(keyListener);
        return this;
    }

    public static Window build(Class<? extends Window> clazz, int width, int height, String renderer)
    {
        return new Builder(clazz, width, height, renderer).build();
    }

    public static Window build(Class<? extends Window> clazz, int width, int height)
    {
        return new Builder(clazz, width, height).build();
    }

    public static Window build(Class<? extends Window> clazz, String renderer)
    {
        return new Builder(clazz, renderer).build();
    }

    public static Window build(Class<? extends Window> clazz)
    {
        return new Builder(clazz).build();
    }

    public static class Builder
    {
        private Map<String, Object> initialization = new HashMap<>();
        private Class<? extends Window> windowClass;

        public Builder(Class<? extends Window> clazz, int width, int height, String renderer)
        {
            this(clazz, width, height);
            initialization.put("renderer", renderer);
        }

        public Builder(Class<? extends Window> clazz, int width, int height)
        {
            this(clazz);
            initialization.put("width", width);
            initialization.put("height", height);
        }

        public Builder(Class<? extends Window> clazz, String renderer)
        {
            this(clazz);
            initialization.put("renderer", renderer);
        }

        public Builder(Class<? extends Window> clazz)
        {
            windowClass = clazz;
        }

        public <T> Builder add(String name, T content)
        {
            initialization.put(name, content);
            return this;
        }

        public Window build()
        {
            try
            {
                Constructor<? extends Window> windowConstructor = windowClass.getConstructor();
                Window window = windowConstructor.newInstance();
                window.addListener(window);
                PApplet.runSketch(new String[] { windowClass.getName() }, window.setBuilder(this));
                return window;
            }
            catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
			        | IllegalArgumentException | InvocationTargetException e)
            {
                throw new RuntimeException(e);
            }
        }

        public <T> T get(String name)
        {
            @SuppressWarnings("unchecked")
		    T value = (T) initialization.get(name);
		    return value;
        }

        public boolean is(Class<?> clazz, String... names)
        {
            for (String name : names)
            {
                Object object = initialization.get(name);
                if (object == null || !object.getClass().equals(clazz))
                    return false;
            }
            return true;
        }
    }
}