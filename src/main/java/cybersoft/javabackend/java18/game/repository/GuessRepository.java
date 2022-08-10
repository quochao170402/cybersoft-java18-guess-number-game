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
}
