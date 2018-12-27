package tests.trexgame;

import engine.base.Game;

import tests.trexgame.states.MainState;
import tests.trexgame.states.LostState;

public class TrexGame extends Game
{
    public TrexGame()
    {
        addGameState(new MainState());
    }
}