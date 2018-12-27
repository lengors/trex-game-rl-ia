package engine.base;

public abstract class GameState
{
    private Game game;

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

    public abstract GameState update();
}