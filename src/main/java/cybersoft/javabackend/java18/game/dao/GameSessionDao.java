package cybersoft.javabackend.java18.game.dao;

import cybersoft.javabackend.java18.game.model.GameSession;

import java.util.List;

public interface GameSessionDao {
    boolean insert(GameSession gameSession);
    List<GameSession> findByUsername(String username);
    List<GameSession> findFinishedGame();
    void deactivateAllGameByUsername(String username);

    int count();

    void finishedGame(GameSession gameSession);
}
