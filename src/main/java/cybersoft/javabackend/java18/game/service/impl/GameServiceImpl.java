package cybersoft.javabackend.java18.game.service.impl;

import cybersoft.javabackend.java18.game.model.GameSession;
import cybersoft.javabackend.java18.game.model.Guess;
import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.repository.GameSessionRepository;
import cybersoft.javabackend.java18.game.repository.GuessRepository;
import cybersoft.javabackend.java18.game.repository.PlayerRepository;
import cybersoft.javabackend.java18.game.repository.impl.GameSessionRepositoryImpl;
import cybersoft.javabackend.java18.game.repository.impl.GuessRepositoryImpl;
import cybersoft.javabackend.java18.game.repository.impl.PlayerRepositoryImpl;
import cybersoft.javabackend.java18.game.service.GameService;

import java.util.List;

public class GameServiceImpl implements GameService {
    private static GameServiceImpl service = null;

    private final GameSessionRepository gameSessionRepository;
    private final PlayerRepository playerRepository;
    private final GuessRepository guessRepository;

    // total game in database. using it to set game session id
    private int indexId;


    private GameServiceImpl() {
        this.gameSessionRepository = GameSessionRepositoryImpl.getRepository();
        this.playerRepository = PlayerRepositoryImpl.getRepository();
        this.guessRepository = GuessRepositoryImpl.getRepository();
        indexId = gameSessionRepository.countGames() + 1;

    }

    public static GameServiceImpl getService() {
        if (service == null) {
            service = new GameServiceImpl();
        }
        return service;
    }


    @Override
    public Player login(String username, String password) {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            return null;
        }

        return player.getPassword().equals(password) ? player : null;
    }


    @Override
    public Player register(String username, String password, String name) {
        if (!isValidPlayer(username, password, name)) return null;

        // check player existed
        boolean existedPlayer = playerRepository.existedByUsername(username);

        if (existedPlayer)
            return null;

        Player player = new Player(username, password, name);

        // save player to store
        if (playerRepository.insert(player)) {
            return player;
        }

        return null;
    }

    /**
     * Validate user input
     *
     * @param username player username
     * @param password player password
     * @param name     player name
     * @return true if valid or false
     */
    private boolean isValidPlayer(String username, String password, String name) {
        if (username == null || "".equals(username.trim()))
            return false;

        if (password == null || "".equals(password.trim()))
            return false;

        if (name == null || "".equals(name.trim()))
            return false;

        return true;
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
    public GameSession findGameById(String id) {
        GameSession gameSession = gameSessionRepository.findById(id);
        gameSession.setGuesses(guessRepository.findByGameId(gameSession.getId()));
        return gameSession;
    }

    @Override
    public List<Guess> findGuessesByGameIdWithPagination(String gameId, int page) {
        return guessRepository.findByGameIdWithPagination(gameId, page);
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
    public int countGuessesByGameId(String gameId) {
        return guessRepository.countGuessesByGameId(gameId);
    }

    @Override
    public int countGamesByUsername(String username) {
        return gameSessionRepository.countGamesByUsername(username);
    }

    @Override
    public void setGameActiveById(String gameId, boolean isActive) {
        if (isActive) {
            GameSession gameSession = findGameById(gameId);
            gameSessionRepository.deactivateAllGameByUsername(gameSession.getUsername());
        }
        gameSessionRepository.updateActiveById(gameId, isActive);
    }
}
