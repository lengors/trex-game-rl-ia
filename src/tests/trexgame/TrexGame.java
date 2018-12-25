package tests.trexgame;

import tests.trexgame.states.LostState;
import tests.trexgame.states.MainState;

public class TrexGame extends Game
{
    public TrexGame()
    {
        register(GameState.build(LostState.class));
        register(GameState.build(MainState.class));
    }
}