package cybersoft.javabackend.java18.gamedoanso.repository;

import cybersoft.javabackend.java18.gamedoanso.model.Player;
import cybersoft.javabackend.java18.gamedoanso.store.GameStoreHolder;

import java.util.List;

public class PlayerRepository {

    private final List<Player> players;

    public PlayerRepository() {
        players = GameStoreHolder.getStore().getPlayers();
    }

    public Player findByUsername(String username) {
        return players.stream()
                .filter(player -> player.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public void save(Player player) {
        players.add(player);
    }

    public boolean existedByUsername(String username) {
        return players.stream()
                .anyMatch(player -> player.getUsername().equals(username));
    }
}
