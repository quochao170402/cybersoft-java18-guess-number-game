package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.dao.GameSessionDao;
import cybersoft.javabackend.java18.game.dao.impl.GameSessionDaoImpl;
import cybersoft.javabackend.java18.game.model.GameSession;

import java.util.List;

public class GameSessionRepository {

    private final GameSessionDao dao;

    public GameSessionRepository() {
        dao = GameSessionDaoImpl.getInstance();
    }

    public void save(GameSession gameSession) {
        dao.insert(gameSession);
    }

    public List<GameSession> findByUsername(String username) {
        return dao.findByUsername(username);
    }

    public List<GameSession> findFinishedGames() {
        return dao.findFinishedGame();
    }

    public void deactivateAllGameByUsername(String username){
        dao.deactivateAllGameByUsername(username);
    }

    public int count() {
        return dao.count();
    }

    public void finished(GameSession gameSession) {
        dao.finishedGame(gameSession);
    }
}
