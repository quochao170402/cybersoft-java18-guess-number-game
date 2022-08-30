package cybersoft.javabackend.java18.game.service;

import cybersoft.javabackend.java18.game.model.Player;

public interface AuthService {

    /**
     * Sign in method to get a user from database by username and password
     *
     * @param username player username
     * @param password player password
     * @return a Player instance if found or null
     */
    Player login(String username, String password);

    /**
     * Sign up method to add new player to database if not existed
     *
     * @param username player username
     * @param password player password
     * @param name     player name
     * @return a Player instance if user input is valid and not duplicate or null
     */
    Player register(String username, String password, String name);


    Player findPlayerByUsername(String username);
}
