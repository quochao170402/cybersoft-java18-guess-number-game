package cybersoft.javabackend.java18.game.service;

import cybersoft.javabackend.java18.game.model.GameSession;
import cybersoft.javabackend.java18.game.model.Guess;
import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.repository.GameSessionRepository;
import cybersoft.javabackend.java18.game.repository.GuessRepository;
import cybersoft.javabackend.java18.game.repository.PlayerRepository;
import cybersoft.javabackend.java18.game.repository.impl.GameSessionRepositoryImpl;
import cybersoft.javabackend.java18.game.repository.impl.GuessRepositoryImpl;
import cybersoft.javabackend.java18.game.repository.impl.PlayerRepositoryImpl;

import java.util.List;

public class GameService {
    private static GameService service = null;

    private final GameSessionRepository gameSessionRepository;
    private final PlayerRepository playerRepository;
    private final GuessRepository guessRepository;

    // total player in database. using it to set game session id
    private int indexId;


    private GameService() {
        this.gameSessionRepository = GameSessionRepositoryImpl.getRepository();
        this.playerRepository = PlayerRepositoryImpl.getRepository();
        this.guessRepository = GuessRepositoryImpl.getRepository();
        indexId = gameSessionRepository.getNumberOfRecordGameSessionTable() + 1;

    }

    public static GameService getService() {
        if (service == null) {
            service = new GameService();
        }
        return service;
    }

    /**
     * Sign in method to get a user from database by username and password
     *
     * @param username player username
     * @param password player password
     * @return a Player instance if found or null
     */
    public Player signIn(String username, String password) {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            return null;
        }

        return player.getPassword().equals(password) ? player : null;
    }

    /**
     * Sign up method to add new player to database if not existed
     *
     * @param username player username
     * @param password player password
     * @param name     player name
     * @return a Player instance if user input is valid and not duplicate or null
     */
    public Player signUp(String username, String password, String name) {
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

    /**
     * Create new game and deactivate all other
     * game of player by username then save game to database
     *
     * @param username username of player need create new game
     * @return new game session
     */
    public GameSession createGame(String username) {
        GameSession gameSession = new GameSession(username, indexId++);
        gameSession.setActive(true);

        // deactivate all other game
        gameSessionRepository.deactivateAllGameByUsername(username);

        // save game to store
        gameSessionRepository.insert(gameSession);
        return gameSession;
    }

    /**
     * Get the active game by username, if not found create a new game
     *
     * @param username player's username
     * @return current active game of player
     */
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

    /**
     * Ranking completed game by game's guess and complete time with pagination
     *
     * @param page Current page need get data
     * @return sorted game list
     */
    public List<GameSession> rankingByPagination(int page) {
        // get completed games with pagination
        List<GameSession> games = gameSessionRepository.rankingWithPagination(page);

        // get guesses for games
        games.forEach(game -> game.setGuesses(guessRepository.findByGameId(game.getId())));

        return games;
    }

    /**
     * Method get size of records in view ranking to calculate total page
     *
     * @return size of records
     */
    public int getSizeOfRank() {
        return gameSessionRepository.getNumberOfRecordRankingView();
    }

    public Guess guessNumber(GameSession currentGame, int number) {
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
}
