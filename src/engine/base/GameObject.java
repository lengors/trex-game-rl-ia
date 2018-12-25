package engine.base;

import java.util.Map;
import java.util.Set;

import engine.ecs.Observable;

import java.util.HashMap;
import java.util.Collection;

import java.lang.reflect.InvocationTargetException;

public class GameObject extends Observable implements Map<String, Object>
{
    private Map<String, Object> objects;

    public GameObject(Class<? extends Map<String, Object>> mapClass)
    {
        try
        {
            objects = mapClass.getConstructor().newInstance();
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    public GameObject()
    {
        objects = new HashMap<>();
    }

    @Override
    public void clear()
    {
        objects.clear();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return objects.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return objects.containsValue(value);
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet()
    {
        return objects.entrySet();
    }

    @Override
    public boolean equals(Object o)
    {
        return objects.equals(o);
    }

    @Override
    public Object get(Object key)
    {
        return objects.get(key);
    }

    @Override
    public int hashCode()
    {
        return objects.hashCode();
    }

    @Override
    public boolean isEmpty()
    {
        return objects.isEmpty();
    }

    @Override
    public Set<String> keySet()
    {
        return objects.keySet();
    }

    @Override
    public Object put(String key, Object value)
    {
        return objects.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m)
    {
        objects.putAll(m);
    }

    @Override
    public Object remove(Object key)
    {
        return objects.remove(key);
    }

    @Override
    public int size()
    {
        return objects.size();
    }

    @Override
    public Collection<Object> values()
    {
        return objects.values();
    }
}