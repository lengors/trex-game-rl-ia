package engine.base;

public abstract class GameObject
{
    private Game game;

    public Game getGame()
    {
        return game;
    }

    public GameObject setGame(Game game)
    {
        this.game = game;
        return this;
    }
}