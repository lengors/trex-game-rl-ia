package engine.graphics;

import java.util.Map;
import java.util.HashMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import processing.core.PApplet;

public class Window extends PApplet
{
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

    public Window setBuilder(Builder builder)
    {
        this.builder = builder;
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