package cybersoft.javabackend.java18.game.service.impl;

import cybersoft.javabackend.java18.game.model.GameSession;
import cybersoft.javabackend.java18.game.model.Guess;
import cybersoft.javabackend.java18.game.repository.GameSessionRepository;
import cybersoft.javabackend.java18.game.repository.GuessRepository;
import cybersoft.javabackend.java18.game.repository.impl.GameSessionRepositoryImpl;
import cybersoft.javabackend.java18.game.repository.impl.GuessRepositoryImpl;
import cybersoft.javabackend.java18.game.service.GuessService;

import java.util.List;

public class GuessServiceImpl implements GuessService {
    private static GuessService guessService = null;

    private final GuessRepository guessRepository;
    private final GameSessionRepository gameSessionRepository;

    private GuessServiceImpl() {
        guessRepository = GuessRepositoryImpl.getRepository();
        gameSessionRepository = GameSessionRepositoryImpl.getRepository();
    }

    public static GuessService getService() {
        if (guessService == null) guessService = new GuessServiceImpl();
        return guessService;
    }

    @Override
    public Guess guessingNumber(GameSession currentGame, int number) {
        // guess number and save guess to db
        int targetNumber = currentGame.getTargetNumber();
        Guess guess = new Guess(currentGame.getId(), number, 0);

        // Check user number with target number
        if (targetNumber == number) {
            guess.setResult(0);

            // update finished game and deactivate it.
            currentGame.finished();
            gameSessionRepository.completedGameById(currentGame.getId());
        } else if (targetNumber < number) {
            guess.setResult(1);
        } else {
            guess.setResult(-1);
        }

        // save guess to db
        guessRepository.insert(guess);
        return guess;
    }

    @Override
    public List<Guess> findGuessesByGameIdWithPagination(String gameId, int page) {
        return guessRepository.findByGameIdWithPagination(gameId, page);
    }

    @Override
    public int countGuessesByGameId(String gameId) {
        return guessRepository.countGuessesByGameId(gameId);
    }

}
