package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.dao.PlayerDao;
import cybersoft.javabackend.java18.game.dao.impl.PlayerDaoImpl;
import cybersoft.javabackend.java18.game.model.Player;

public class PlayerRepository {

    private final PlayerDao dao;

    public PlayerRepository() {
        dao = PlayerDaoImpl.getInstance();
    }

    public Player findByUsername(String username) {
        return dao.findByUsername(username);
    }

    public boolean save(Player player) {
        return dao.insert(player);
    }

    public boolean existedByUsername(String username) {
        return dao.existedByUsername(username);
    }
}
