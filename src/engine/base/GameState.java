package engine.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class GameState
{
    private Game game;

    public abstract void onUnregister();
    public abstract void onRegister();
    public abstract void onEnter();
    public abstract void onExit();

    public abstract String onRun();

    public Game getGame()
    {
        return game;
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }

    public GameState setGame(Game game)
    {
        this.game = game;
        return this;
    }

    public static GameState build(Class<? extends GameState> gameStateClass)
    {
        try
        {
            Constructor<? extends GameState> constructor = gameStateClass.getConstructor();
            GameState gameState = constructor.newInstance();
            return gameState;
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
			        | IllegalArgumentException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }
}