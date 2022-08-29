package cybersoft.javabackend.java18.game.service;

import cybersoft.javabackend.java18.game.model.GameSession;
import cybersoft.javabackend.java18.game.model.Guess;

import java.util.List;

public interface GuessService {
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
}
