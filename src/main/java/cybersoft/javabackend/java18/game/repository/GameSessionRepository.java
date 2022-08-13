package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.model.GameSession;

import java.util.List;

public interface GameSessionRepository {

    /**
     * Insert a game session to database
     *
     * @param gameSession values to insert
     */
    void insert(GameSession gameSession);

    /**
     * Find all game session by username
     *
     * @param username Player username
     * @return List of game session or empty list
     */
    List<GameSession> findByUsername(String username);

    /**
     * Deactivate all games by player username
     *
     * @param username Player username
     */
    void deactivateAllGameByUsername(String username);

    /**
     * Set a game session was completed by game session id
     *
     * @param id Game session id
     */
    void completedGameById(String id);

    /**
     * Get number of records of game session table in database
     *
     * @return Number of records if successful or 0
     */
    int countGames();

    /**
     * Ranking completed games with pagination
     *
     * @param page Page need get data
     * @return Sorted list game session in this page
     */
    List<GameSession> rankingWithPagination(int page);

    /**
     * Get number of records of raking view in database
     *
     * @return Number of records if successful or 0
     */
    public int getNumberOfRecordRankingView();

    /**
     * Update game active or inactive
     *
     * @param gameId Game session id
     * @param active Active or inactive
     */
    void updateActiveById(String gameId, boolean active);

    /**
     * Find game session by game session id
     *
     * @param id Id game
     * @return A game if found or null
     */
    GameSession findById(String id);

    /**
     * Find games by username with pagination
     *
     * @param username Player's username
     * @param page     current page
     * @return List games if found or empty list
     */
    List<GameSession> findByUsernameWithPagination(String username, int page);

    /**
     * Count number of games by player's username
     *
     * @param username player's username
     * @return number of games
     */
    int countGamesByUsername(String username);
}
