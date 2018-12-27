package engine.base;

import java.lang.reflect.InvocationTargetException;

import engine.listeners.DefaultListener;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class Game extends DefaultListener implements Runnable
{
    private Map<Class<?>, List<Object>> resources = new HashMap<>();
    private Map<String, GameState> gameStates = new HashMap<>();
    private List<GameObject> gameObjects = new ArrayList<>();
    private volatile boolean running;
    private GameState gameState;

    public <T extends GameObject> T addGameObject(Class<T> gameObjectClass)
    {
        try
        {
            return (T) gameObjectClass.getConstructor().newInstance().setGame(this);
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Game addGameObject(GameObject gameObject)
    {
        gameObjects.add(gameObject.setGame(this));
        return this;
    }

    public boolean addGameState(GameState gameState)
    {
        if (gameState != null)
        {
            gameStates.put(gameState.getName(), gameState.setGame(this));
            if (this.gameState == null)
                this.gameState = gameState;
            return true;
        }
        return false;
    }

    public Game addResource(Object resource)
    {
        List<Object> list = resources.get(resource.getClass());
        if (list == null)
            resources.put(resource.getClass(), list = new ArrayList<>());
        list.add(resource);
        return this;
    }

    public Game addResources(Object... resources)
    {
        for (Object resource : resources)
            addResource(resource);
        return this;
    }

    public GameObject getGameObject(int index)
    {
        return gameObjects.get(index);
    }

    public int getGameObjectCount()
    {
        return gameObjects.size();
    }

    public int getGameObjectIndex(GameObject gameObject)
    {
        return gameObjects.indexOf(gameObject);
    }

    public List<GameObject> getGameObjects()
    {
        return new ArrayList<>(gameObjects);
    }

    public <T extends GameObject> List<T> getGameObjects(Class<T> gameObjectClass)
    {
        List<T> specializedGameObjects = new ArrayList<>();
        for (GameObject gameObject : gameObjects)
            if (gameObject.getClass().isAssignableFrom(gameObjectClass))
                specializedGameObjects.add((T) gameObject);
        return specializedGameObjects;
    }

    public GameState getGameState(String name)
    {
        return gameStates.get(name);
    }

    public int getGameStateCount()
    {
        return gameStates.size();
    }

    public List<GameState> getGameStates()
    {
        return new ArrayList<>(gameStates.values());
    }

    public <T> T getResource(Class<T> resourceClass, int index)
    {
        return (T) resources.get(resourceClass).get(index);
    }

    public <T> T getResource(Class<T> resourceClass)
    {
        return getResource(resourceClass, 0);
    }

    public <T> List<T> getResources(Class<T> resourcesClass)
    {
        List<Object> list = resources.get(resourcesClass);
        List<T> result = new ArrayList<>(list.size());
        for (Object object : list)
            result.add((T) object);
        return result;
    }

    public List<Object> getResources()
    {
        List<Object> rs = new ArrayList<>();
        for (List<Object> list : resources.values())
            rs.addAll(list);
        return rs;
    }

    public Thread makeThreadable()
    {
        return new Thread(this);
    }

    public <T> T popResource(Class<T> resourceClass, int index)
    {
        List<Object> rs = resources.get(resourceClass);
        T resource = (T) rs.remove(index);
        if (rs.size() == 0)
            resources.remove(resourceClass);
        return resource;
    }
    
    public <T> T popResource(Class<T> resourceClass)
    {
        return popResource(resourceClass, 0);
    }

    public GameObject removeGameObject(int index)
    {
        return gameObjects.remove(index).setGame(null);
    }

    public boolean removeGameObject(GameObject gameObject)
    {
        boolean returnValue = gameObjects.remove(gameObject);
        if (returnValue)
            gameObject.setGame(null);
        return returnValue;
    }

    public GameState removeGameState(String name)
    {
        return gameStates.remove(name).setGame(null);
    }

    public boolean removeGameState(GameState gameState)
    {
        if (gameState != null)
        {
            boolean returnValue = gameStates.remove(gameState.getName()) == gameState;
            if (returnValue)
                gameState.setGame(null);
            return returnValue;
        }
        return false;
    }

    public <T> List<T> removeResources(Class<T> resourcesClass)
    {
        List<Object> list = resources.remove(resourcesClass);
        List<T> result = new ArrayList<>(list.size());
        for (Object object : list)
            result.add((T) object);
        return result;
    }

    public boolean removeResource(Object resource)
    {
        List<Object> rs = resources.get(resource.getClass());
        if (rs != null)
        {
            boolean returnValue = rs.remove(resource);
            if (rs.size() == 0)
                resources.remove(resource.getClass());
            return returnValue;
        }
        return false;
    }

    @Override
    public void run()
    {
        running = true;
        for (GameObject gameObject : gameObjects)
            gameObject.setup();
        while (gameState != null && running)
        {
            gameState = gameState.update();
            for (GameObject gameObject : gameObjects)
                gameObject.update();
        }
    }

    public void stop()
    {
        running = false;
    }
}