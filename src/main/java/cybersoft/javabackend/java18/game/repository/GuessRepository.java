package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.model.Guess;

import java.util.List;

public interface GuessRepository {

    /**
     * Get all guesses by game session id
     *
     * @param gameId game session id
     * @return List of guesses
     */
    List<Guess> findByGameId(String gameId);

    /**
     * Insert guess to database
     *
     * @param guess guess need insert
     */
    void insert(Guess guess);

    /**
     * Find guesses by game id with pagination
     *
     * @param gameId Game's id
     * @param page   current page
     * @return List guesses with pagination
     */
    List<Guess> findByGameIdWithPagination(String gameId, int page);

    /**
     * Count number of guesses by game's id
     *
     * @param gameId game's id
     * @return number of guesses
     */
    int countGuessesByGameId(String gameId);
}
