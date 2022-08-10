package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.model.Player;

public interface PlayerRepository {
    /**
     * Insert data of player to database
     *
     * @param player data will insert to database
     * @return true if insert successful or false
     */
    boolean insert(Player player);

    /**
     * Check player was existed by player username
     *
     * @param username player username
     * @return true if player existed or false
     */
    boolean existedByUsername(String username);

    /**
     * Find player by username
     *
     * @param username player username
     * @return a player instance if found or null
     */
    Player findByUsername(String username);
}
