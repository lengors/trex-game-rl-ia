package tests.trexgame.states;

import engine.base.GameState;

import tests.trexgame.objects.TrexObject;

import tests.trexgame.objects.obstacles.Obstacle;

import java.util.List;

public class MainState extends GameState
{
    @Override
    public GameState update()
    {
        List<Obstacle> obstacles = getGame().getGameObjects(Obstacle.class);
        int counter = obstacles.size();
        while (counter++ < 2)
        {
            Obstacle obstacle = Obstacle.getRandomObstacle();
            obstacle.setGame(getGame()).setup();
            getGame().addGameObject(obstacle);
        }
        for (Obstacle obstacle : obstacles)
            if (obstacle.getPosition().x + obstacle.getTexture().width / 2 <= 0)
                getGame().removeGameObject(obstacle);
        if (obstacles.size() > 0)
        {
            Obstacle obstacle = obstacles.get(0);
            List<TrexObject> trexs = getGame().getGameObjects(TrexObject.class);
            for (TrexObject trex : trexs)
                if (trex.collides(obstacle))
                    getGame().removeGameObject(trex);
        }
        return this;
    }
}