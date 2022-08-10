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
    int getNumberOfRecordGameSessionTable();

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
}
