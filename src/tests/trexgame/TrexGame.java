package tests.trexgame;

import java.util.List;

import engine.base.Game;

import tests.trexgame.states.MainState;

import tests.trexgame.objects.obstacles.Obstacle;

public class TrexGame extends Game
{
    private Obstacle obstacle;

    public TrexGame()
    {
        addGameState(new MainState());
    }

    public Obstacle getObstacle()
    {
        if (obstacle == null)
        {
            List<Obstacle> obstacles = getGameObjects(Obstacle.class);
            if (obstacles.size() > 0)
                obstacle = obstacles.get(0);
        }
        else
        {
            float oX = obstacle.getPosition().x - obstacle.getWidth() / 2;
            if (oX < 30)
            {
                List<Obstacle> obstacles = getGameObjects(Obstacle.class);
                if (obstacles.size() > 1)
                    obstacle = obstacles.get(1);
            }
        }
        return obstacle;
    }
}