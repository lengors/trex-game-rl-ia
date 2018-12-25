package engine.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class Game implements Runnable
{
    private Map<String, GameState> gameStates = new HashMap<>();
    private List<GameObject> gameObjects = new ArrayList<>();

    private GameState gameState;
    private String initialState;
    private Thread thread;

    public Game addObject(GameObject gameObject)
    {
        gameObjects.add(gameObject);
        return this;
    }

    public GameObject getObject(int index)
    {
        return gameObjects.get(index);
    }

    public int getObjectCount()
    {
        return gameObjects.size();
    }

    public <T extends GameObject> T getObject(Class<T> objectClass, int index)
    {
        for (GameObject gameObject : gameObjects)
            if (gameObject.getClass().isAssignableFrom(objectClass))
            {
                if (index == 0)
                    return (T) gameObject;
                else
                    index -= 1;
            }
        return null;
    }

    public <T extends GameObject> T getObject(Class<T> objectClass)
    {
        return getObject(objectClass, 0);
    }

    public List<GameObject> getObjects()
    {
        return new ArrayList<>(gameObjects);
    }

    public <T extends GameObject> List<T> getObjects(Class<T> objectClass)
    {
        List<T> objects = new ArrayList<>();
        for (GameObject gameObject : gameObjects)
            if (gameObject.getClass().isAssignableFrom(objectClass))
                objects.add((T) gameObject);
        return objects;
    }

    public boolean removeObject(GameObject gameObject)
    {
        return gameObjects.remove(gameObject);
    }

    public GameObject removeObject(int index)
    {
        return gameObjects.remove(index);
    }

    public void join()
    {
        try
        {
            if (thread != null)
                thread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Game register(String stateName, GameState gameState)
    {
        gameStates.put(stateName, gameState);
        gameState.setGame(this);
        gameState.onRegister();
        if (initialState == null)
            initialState = stateName;
        return this;
    }

    public Game register(GameState gameState)
    {
        return register(gameState.getName(), gameState);
    }

    public Game unregister(GameState gameState)
    {
        return unregister(gameState.getName());
    }

    public Game unregister(String stateName)
    {
        GameState gameState = gameStates.remove(stateName);
        gameState.onUnregister();
        gameState.setGame(null);
        return this;
    }

    public Game setInitialState(String initialState)
    {
        this.initialState = initialState;
        return this;
    }

    @Override
    public void run()
    {
        String stateName = initialState;
        gameState = get(stateName, null);
        while (gameState != null)
        {
            stateName = gameState.onRun();
            gameState = get(stateName, gameState);
        }
    }

    public void start()
    {
        if (thread != null)
            thread.start();
    }

    public void stop()
    {
        gameState = null;
        join();
    }

    private GameState get(String stateName, GameState gameState)
    {
        GameState newGameState = gameStates.get(stateName);
        if (newGameState != gameState)
        {
            gameState.onExit();
            if (newGameState != null)
                newGameState.onEnter();
        }
        else if (gameState == null)
            newGameState.onEnter();
        return newGameState;
    }

    public Game build(Class<? extends Game> gameClass)
    {
        try
        {
            Constructor<? extends Game> constructor = gameClass.getConstructor();
            Game game = constructor.newInstance();
            Thread thread = new Thread(game);
            game.thread = thread;
            return game;
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
			        | IllegalArgumentException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }
}