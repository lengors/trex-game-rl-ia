package engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.Map;
import java.util.HashMap;

public class Game implements Runnable
{
    private Map<String, GameState> gameStates = new HashMap<>();
    private String initialState;
    private Thread thread;

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
        GameState gameState = get(stateName, null);
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