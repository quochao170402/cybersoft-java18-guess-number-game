package cybersoft.javabackend.java18.gamedoanso.repository;

import cybersoft.javabackend.java18.gamedoanso.model.GameSession;
import cybersoft.javabackend.java18.gamedoanso.store.GameStoreHolder;

import java.util.List;

public class GameSessionRepository {

    private final List<GameSession> gameSessions;

    public GameSessionRepository() {
        gameSessions = GameStoreHolder.getStore().getGames();
    }

    public void save(GameSession gameSession) {
        gameSessions.add(gameSession);
    }

    public List<GameSession> findByUsername(String username) {
        return gameSessions.stream()
                .filter(game -> game.getUsername().equals(username))
                .toList();
    }

    public List<GameSession> findAll() {
        return gameSessions;
    }

    public List<GameSession> findFinishedGames() {
        return gameSessions.stream()
                .filter(GameSession::isCompleted)
                .toList();
    }
}
