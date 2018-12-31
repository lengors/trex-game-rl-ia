package tests.trexgame.states;

import engine.base.GameState;

import engine.learning.NeuralNetwork;

import tests.trexgame.objects.Ground;
import tests.trexgame.objects.TrexObject;

import tests.trexgame.Main.NetworkSelection;

import tests.trexgame.objects.obstacles.Obstacle;

import java.util.List;

public class MainState extends GameState
{
    private static int maxScore = 0;

    @Override
    public GameState update()
    {
        Ground ground = getGame().getGameObjects(Ground.class).get(0);
        int score = ground.getScore();
        if (maxScore < score)
            maxScore = score;
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
            NetworkSelection ns = getGame().getResource(NetworkSelection.class);
            List<TrexObject> trexs = getGame().getGameObjects(TrexObject.class);
            for (int i = 0; i < trexs.size(); ++i)
            {
                TrexObject trex = trexs.get(i);
                if (trex.collides(obstacle))
                {
                    getGame().removeGameObject(trex);
                    ns.setScore(trex.get(NeuralNetwork.class), score);
                }
            }
        }
        return this;
    }

    public static int getMaxScore()
    {
        return maxScore;
    }
}