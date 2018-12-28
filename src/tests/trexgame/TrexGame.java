package tests.trexgame;

import engine.base.Game;

import tests.trexgame.states.MainState;

public class TrexGame extends Game
{
    public TrexGame()
    {
        addGameState(new MainState());
    }
}