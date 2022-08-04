package cybersoft.javabackend.java18.game.dao;

import cybersoft.javabackend.java18.game.model.Player;

public interface PlayerDao {
    Player findByUsername(String username);
    boolean insert(Player player);
    boolean existedByUsername(String username);
}
