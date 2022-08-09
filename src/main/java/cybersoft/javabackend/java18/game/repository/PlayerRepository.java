package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.model.Player;

import java.util.List;

public interface PlayerRepository {
    boolean insert(Player player);

    boolean existedByUsername(String username);

    Player findByUsername(String username);

    List<Player> getAll();
}
