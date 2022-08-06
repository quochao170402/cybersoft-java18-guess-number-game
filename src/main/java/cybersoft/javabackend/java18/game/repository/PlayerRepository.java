package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.model.Player;

public interface PlayerRepository {
    boolean insert(Player player);
    boolean existedByUsername(String username);
    Player findByUsername(String username);
}
