package cybersoft.javabackend.java18.game.service;

import cybersoft.javabackend.java18.game.model.GameSession;

import java.util.List;

public interface GameSessionService {
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
     * Update game active or inactive by game id
     *
     * @param gameId   Game session id
     * @param isActive is game active
     */
    void setGameActiveById(String gameId, boolean isActive);

    /**
     * Count number of game by username
     *
     * @param username Player username
     * @return number of games
     */
    int countGamesByUsername(String username);
}
