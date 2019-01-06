package engine.base;

import java.lang.reflect.InvocationTargetException;

import engine.listeners.DefaultListener;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.concurrent.Semaphore;

public class Game extends DefaultListener implements Runnable
{
    private Map<Class<?>, List<Object>> resources = new HashMap<>();
    private Map<String, GameState> gameStates = new HashMap<>();
    private List<GameObject> gameObjects = new ArrayList<>();
    private Semaphore semaphore = new Semaphore(0);
    private volatile boolean running;
    private double upsNS = 1e9 / 60;
    private GameState gameState;

    public <T extends GameObject> T addGameObject(Class<T> gameObjectClass)
    {
        try
        {
            @SuppressWarnings("unchecked")
			T gameObject = (T) gameObjectClass.getConstructor().newInstance().setGame(this);
            synchronized (gameObjects)
            {
                gameObjects.add(gameObject);
            }
            return gameObject;
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Game addGameObject(GameObject gameObject)
    {
        gameObject.setGame(this);
        synchronized (gameObjects)
        {
            gameObjects.add(gameObject.setGame(this));
        }
        return this;
    }

    public boolean addGameState(GameState gameState)
    {
        if (gameState != null)
        {
            gameState.setGame(this);
            synchronized (gameStates)
            {
                gameStates.put(gameState.getName(), gameState);
            }
            if (this.gameState == null)
                this.gameState = gameState;
            return true;
        }
        return false;
    }

    public Game addResource(Class<?> resourceClass, Object resource)
    {
        synchronized (resources)
        {
            List<Object> list = resources.get(resourceClass);
            if (list == null)
                resources.put(resourceClass, list = new ArrayList<>());
            list.add(resource);
        }
        return this;
    }

    public Game addResource(Object resource)
    {
        return addResource(resource.getClass(), resource);
    }

    public Game addResources(Class<?> resourceClass, Object... rs)
    {
        synchronized (resources)
        {
            List<Object> list = resources.get(resourceClass);
            if (list == null)
                resources.put(resourceClass, list = new ArrayList<>());
            for (Object resource : rs)
                list.add(resource);
        }
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
        GameObject go;
        synchronized (gameObjects)
        {
            go = gameObjects.get(index); 
        }
        return go;
    }

    public int getGameObjectCount()
    {
        int size;
        synchronized (gameObjects)
        {
            size = gameObjects.size();
        }
        return size;
    }

    public int getGameObjectIndex(GameObject gameObject)
    {
        int index;
        synchronized (gameObjects)
        {
            index = gameObjects.indexOf(gameObject);
        }
        return index;
    }

    public List<GameObject> getGameObjects()
    {
        List<GameObject> gos;
        synchronized (gameObjects)
        {
            gos = new ArrayList<>(gameObjects);
        }
        return gos;
    }

    @SuppressWarnings("unchecked")
	public <T extends GameObject> List<T> getGameObjects(Class<T> gameObjectClass)
    {
        List<T> specializedGameObjects = new ArrayList<>();
        synchronized (gameObjects)
        {
            for (int i = 0; i < gameObjects.size(); ++i)
            {
                GameObject gameObject = gameObjects.get(i);
                if (gameObjectClass.isAssignableFrom(gameObject.getClass()))
                    specializedGameObjects.add((T) gameObject);
            }
        }
        return specializedGameObjects;
    }

    public GameState getGameState(String name)
    {
        GameState gs;
        synchronized(gameStates)
        {
            gs = gameStates.get(name);
        }
        return gs;
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public int getGameStateCount()
    {
        int size;
        synchronized (gameStates)
        {
            size = gameStates.size();
        }
        return size;
    }

    public List<GameState> getGameStates()
    {
        List<GameState> gss;
        synchronized (gameStates)
        {
            gss = new ArrayList<>(gameStates.values());
        }
        return gss;
    }

    @SuppressWarnings("unchecked")
	public <T> T getResource(Class<T> resourceClass, int index)
    {
        T value;
        synchronized (resources)
        {
            value = (T) resources.get(resourceClass).get(index);
        }
        return value;
    }

    public <T> T getResource(Class<T> resourceClass)
    {
        return getResource(resourceClass, 0);
    }

    @SuppressWarnings("unchecked")
	public <T> List<T> getResources(Class<T> resourcesClass)
    {
        List<T> result;
        synchronized (resources)
        {
            List<Object> list = resources.get(resourcesClass);
            if (list == null)
                list = new ArrayList<>();
            result = new ArrayList<>(list.size());
            for (Object object : list)
                result.add((T) object);
        }
        return result;
    }

    public List<Object> getResources()
    {
        List<Object> rs = new ArrayList<>();
        synchronized (resources)
        {
            for (List<Object> list : resources.values())
                rs.addAll(list);
        }
        return rs;
    }

    public double getUpsNS()
    {
        return upsNS;
    }

    public Thread makeThreadable()
    {
        return new Thread(this);
    }

    @SuppressWarnings("unchecked")
    public <T> T popResource(Class<T> resourceClass, int index)
    {
        T resource;
        synchronized (resources)
        {
            List<Object> rs = resources.get(resourceClass);
		    resource = (T) rs.remove(index);
            if (rs.size() == 0)
                resources.remove(resourceClass);
        }
        return resource;
    }
    
    public <T> T popResource(Class<T> resourceClass)
    {
        return popResource(resourceClass, 0);
    }

    public GameObject removeGameObject(int index)
    {
        GameObject go;
        synchronized (gameObjects)
        {
            go = gameObjects.remove(index);
        }
        return go.setGame(null);
    }

    public boolean removeGameObject(GameObject gameObject)
    {
        boolean returnValue;
        synchronized (gameObjects)
        {
            returnValue = gameObjects.remove(gameObject);
        }
        if (returnValue)
            gameObject.setGame(null);
        return returnValue;
    }

    public GameState removeGameState(String name)
    {
        GameState gs;
        synchronized (gameStates)
        {
            gs = gameStates.remove(name);
        }
        return gs.setGame(null);
    }

    public boolean removeGameState(GameState gameState)
    {
        if (gameState != null)
        {
            GameState gs;
            synchronized (gameStates)
            {
                gs = gameStates.remove(gameState.getName());
            }
            boolean returnValue = gs == gameState;
            if (returnValue)
                gameState.setGame(null);
            return returnValue;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
	public <T> List<T> removeResources(Class<T> resourcesClass)
    {
        List<T> result;
        synchronized (resources)
        {
            List<Object> list = resources.remove(resourcesClass);
            result = new ArrayList<>(list.size());
            for (Object object : list)
                result.add((T) object);
        }
        return result;
    }

    public boolean removeResource(Object resource)
    {
        boolean returnValue = false;
        synchronized (resources)
        {
            List<Object> rs = resources.get(resource.getClass());
            if (rs != null)
            {
                returnValue = rs.remove(resource);
                if (rs.size() == 0)
                    resources.remove(resource.getClass());
            }
        }
        return returnValue;
    }

    @Override
    public void run()
    {
        running = true;
        for (GameObject gameObject : gameObjects)
            gameObject.setup();
        semaphore.release();

        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameState != null && running)
        {
            /*long now = System.nanoTime();
            delta += (now - lastTime) / upsNS;
            while (delta >= 1)
            {*/
                gameState = gameState.update();
                /*synchronized (gameObjects)
                {*/
                    for (GameObject gameObject : gameObjects)
                        gameObject.update();
                /*}
                --delta;
            }
            lastTime = now;*/
        }
    }

    public void setUPS(double ups)
    {
        upsNS = 1e9 / ups;
    }

    public void stop()
    {
        running = false;
    }

    public void waitForSetup()
    {
        try
        {
            semaphore.acquire();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}