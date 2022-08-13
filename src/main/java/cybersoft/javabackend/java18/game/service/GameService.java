package cybersoft.javabackend.java18.game.service;

import cybersoft.javabackend.java18.game.model.GameSession;
import cybersoft.javabackend.java18.game.model.Guess;
import cybersoft.javabackend.java18.game.model.Player;

import java.util.List;

public interface GameService {
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

    /**
     * Create new game and deactivate all other
     * game of player by username then save game to database
     *
     * @param username username of player need create new game
     * @return new game session
     */
    GameSession createGame(String username);

    /**
     * Get the active game by username, if not found create a new game
     *
     * @param username player's username
     * @return current active game of player
     */
    GameSession getCurrentGame(String username);

    /**
     * Guess the number, if the player's number matches the target number,
     * update current completed to database. Save guess to database.
     *
     * @param gameSession Current game need guessing number
     * @param number      player's number
     * @return A guess instance
     */
    Guess guessingNumber(GameSession gameSession, int number);

    /**
     * Ranking completed game by game's guess and complete time with pagination
     *
     * @param page Current page need get data
     * @return sorted game list
     */
    List<GameSession> rankingWithPagination(int page);

    /**
     * Method get size of records in view ranking to calculate total page
     *
     * @return size of records
     */
    int getSizeOfRank();

    List<GameSession> findGamesByUsernameWithPagination(String username, int page);

    /**
     * Get game session by game session's id
     *
     * @param gameId Game session id
     * @return A game session if found or null
     */
    GameSession findGameById(String gameId);

    /**
     * Get list of guesses by game session id with pagination
     *
     * @param gameId Game session id
     * @param page   current page
     * @return List of guesses
     */
    List<Guess> findGuessesByGameIdWithPagination(String gameId, int page);

    /**
     * Count number of guesses by game session id
     *
     * @param gameId Game session id
     * @return Number of guesses
     */
    int countGuessesByGameId(String gameId);

    /**
     * Count number of game by username
     *
     * @param username Player username
     * @return number of games
     */
    int countGamesByUsername(String username);

    /**
     * Update game active or inactive by game id
     *
     * @param gameId   Game session id
     * @param isActive is game active
     */
    void setGameActiveById(String gameId, boolean isActive);
}
