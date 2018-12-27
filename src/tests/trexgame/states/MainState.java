package tests.trexgame.states;

import engine.base.GameState;

import tests.trexgame.objects.obstacles.Obstacle;

public class MainState extends GameState
{
    @Override
    public GameState update()
    {
        List<Obstacle> obstacles = getGame().getGameObjects(Obstacle.class);
        int counter = obstacles.size();
        while (counter++ < 2)
            getGame().addGameObject(Obstacle.getRandomObstacle());
        for (Obstacle obstacle : obstacles)
            if (obstacle.getPosition().x < getTexture().width / 2)
                getGame().removeGameObject(obstacle);
        return this;
    }
}