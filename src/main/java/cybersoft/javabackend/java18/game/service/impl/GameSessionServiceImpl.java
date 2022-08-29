package cybersoft.javabackend.java18.game.service.impl;

import cybersoft.javabackend.java18.game.model.GameSession;
import cybersoft.javabackend.java18.game.model.Guess;
import cybersoft.javabackend.java18.game.repository.GameSessionRepository;
import cybersoft.javabackend.java18.game.repository.GuessRepository;
import cybersoft.javabackend.java18.game.repository.impl.GameSessionRepositoryImpl;
import cybersoft.javabackend.java18.game.repository.impl.GuessRepositoryImpl;
import cybersoft.javabackend.java18.game.service.GameSessionService;

import java.util.List;

public class GameSessionServiceImpl implements GameSessionService {
    private static GameSessionService gameSessionService = null;

    private final GameSessionRepository gameSessionRepository;
    private final GuessRepository guessRepository;
    private int indexId;

    private GameSessionServiceImpl() {
        this.gameSessionRepository = GameSessionRepositoryImpl.getRepository();
        this.guessRepository = GuessRepositoryImpl.getRepository();
        indexId = gameSessionRepository.countGames() + 1;
    }

    public static GameSessionService getService() {
        if (gameSessionService == null) gameSessionService = new GameSessionServiceImpl();
        return gameSessionService;
    }

    @Override
    public GameSession createGame(String username) {
        GameSession gameSession = new GameSession(username, indexId++);
        gameSession.setActive(true);

        // deactivate all other game
        gameSessionRepository.deactivateAllGameByUsername(username);

        // save game to store
        gameSessionRepository.insert(gameSession);
        return gameSession;
    }

    @Override
    public GameSession getCurrentGame(String username) {
        List<GameSession> gameSessions = gameSessionRepository.findByUsername(username);

        //get current active game, if there's no game -> create new one
        GameSession activeGameSession =
                gameSessions.size() == 0
                        ? createGame(username)
                        : gameSessions.stream()
                        .filter(GameSession::isActive)
                        .findFirst()
                        .orElseGet(() -> createGame(username));

        // get guesses and add to this game
        List<Guess> guesses = guessRepository.findByGameId(activeGameSession.getId());
        activeGameSession.setGuesses(guesses);
        return activeGameSession;
    }


    @Override
    public List<GameSession> rankingWithPagination(int page) {
        // get completed games with pagination
        List<GameSession> games = gameSessionRepository.rankingWithPagination(page);

        // get guesses for games
        games.forEach(game -> game.setGuesses(guessRepository.findByGameId(game.getId())));

        return games;
    }

    @Override
    public int getSizeOfRank() {
        return gameSessionRepository.getNumberOfRecordRankingView();
    }

    @Override
    public List<GameSession> findGamesByUsernameWithPagination(String username, int page) {
        // get completed games with pagination
        List<GameSession> games = gameSessionRepository.findByUsernameWithPagination(username, page);

        // get guesses for games
        games.forEach(game -> game.setGuesses(guessRepository.findByGameId(game.getId())));

        return games;
    }

    @Override
    public GameSession findGameById(String id) {
        GameSession gameSession = gameSessionRepository.findById(id);
        gameSession.setGuesses(guessRepository.findByGameId(gameSession.getId()));
        return gameSession;
    }

    @Override
    public void setGameActiveById(String gameId, boolean isActive) {
        if (isActive) {
            GameSession gameSession = findGameById(gameId);
            gameSessionRepository.deactivateAllGameByUsername(gameSession.getUsername());
        }
        gameSessionRepository.updateActiveById(gameId, isActive);
    }

    @Override
    public int countGamesByUsername(String username) {
        return gameSessionRepository.countGamesByUsername(username);
    }
}
