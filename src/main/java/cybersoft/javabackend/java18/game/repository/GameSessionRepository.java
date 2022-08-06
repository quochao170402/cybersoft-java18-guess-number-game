package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.model.GameSession;

import java.util.List;

public interface GameSessionRepository {
    void insert(GameSession gameSession);

    List<GameSession> findByUsername(String username);

    List<GameSession> findFinishedGames();

    void deactivateAllGameByUsername(String username);

    void finishedGameById(String id);

    int count();


}
