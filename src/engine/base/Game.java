package engine.base;

import java.lang.reflect.InvocationTargetException;

import engine.listeners.DefaultListener;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class Game extends DefaultListener implements Runnable
{
    private Map<String, GameState> gameStates = new HashMap<>();
    private List<GameObject> gameObjects = new ArrayList<>();
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

    public Thread makeThreadable()
    {
        return new Thread(this);
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

    @Override
    public void run()
    {
        while (gameState != null)
            gameState = gameState.update();
    }

    public void stop()
    {
        gameState = null;
    }
}