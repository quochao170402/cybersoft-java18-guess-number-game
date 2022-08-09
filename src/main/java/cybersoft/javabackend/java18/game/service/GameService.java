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

import java.util.Comparator;
import java.util.List;

public class GameService {
    private static GameService service = null;

    private final GameSessionRepository gameSessionRepository;
    private final PlayerRepository playerRepository;
    private final GuessRepository guessRepository;
    private int indexId;


    private GameService() {
        this.gameSessionRepository = GameSessionRepositoryImpl.getRepository();
        this.playerRepository = PlayerRepositoryImpl.getRepository();
        this.guessRepository = GuessRepositoryImpl.getRepository();
        indexId = gameSessionRepository.count() + 1;

    }

    public static GameService getService() {
        if (service == null) {
            service = new GameService();
        }
        return service;
    }

    /**
     * Sign in method to get a user from store by username and password
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
     * Sign up method to add new player to store if not existed
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
     * method check validation user input
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
     * Method to create new game and deactivate all other
     * game of player by username then save game to store
     *
     * @param username username of player need create new game
     * @return new game instance
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
     * Method to get the player's active games by username
     *
     * @param username player username
     * @return current active player's game
     */
    public GameSession getCurrentGame(String username) {
        List<GameSession> gameSessions = gameSessionRepository.findByUsername(username);

        //pseudocode
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
     * Get all finished games and sort it by game's guess and complete time
     *
     * @return sorted game list
     */
    public List<GameSession> rankingByPagination(int page) {
        // get all completed game
        List<GameSession> games = gameSessionRepository.rankingByPagination(page);

        // get guesses for games
        games.forEach(game -> game.setGuesses(guessRepository.findByGameId(game.getId())));

        return games;
    }

    public int getSizeOfRank() {
        return gameSessionRepository.getRankSize();
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
            gameSessionRepository.finishedGameById(currentGame.getId());
        } else if (targetNumber < number) {
            guess.setResult(1);
        } else {
            guess.setResult(-1);
        }

        // add guess to game's guess list
        currentGame.getGuesses().add(0, guess);

        // save guess to db
        guessRepository.insert(guess);
        return guess;
    }

    private static class GameComparator implements Comparator<GameSession> {

        @Override
        public int compare(GameSession o1, GameSession o2) {
            if (o1.getGuesses().size() > o2.getGuesses().size()) {
                return 1;
            } else if (o1.getGuesses().size() < o2.getGuesses().size()) {
                return -1;
            } else {
                return Float.compare(o1.getTime(), o2.getTime());
            }
        }
    }

    public static class GuessResult {
        private final static String CORRECT = "Chính xác";
        private final static String LESSER = "Số của bạn nhỏ hơn số ngẫu nhiên";
        private final static String GREATER = "Số của bạn lớn hơn số ngẫu nhiên";
    }
}
