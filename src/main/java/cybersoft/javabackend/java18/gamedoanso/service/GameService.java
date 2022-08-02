package cybersoft.javabackend.java18.gamedoanso.service;

import cybersoft.javabackend.java18.gamedoanso.Game;
import cybersoft.javabackend.java18.gamedoanso.model.GameSession;
import cybersoft.javabackend.java18.gamedoanso.model.Guess;
import cybersoft.javabackend.java18.gamedoanso.model.Player;
import cybersoft.javabackend.java18.gamedoanso.repository.GameSessionRepository;
import cybersoft.javabackend.java18.gamedoanso.repository.GuessRepository;
import cybersoft.javabackend.java18.gamedoanso.repository.PlayerRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GameService {
    private static GameService service = null;

    private final GameSessionRepository gameSessionRepository;
    private final PlayerRepository playerRepository;
    private final GuessRepository guessRepository;


    private GameService() {
        this.gameSessionRepository = new GameSessionRepository();
        this.playerRepository = new PlayerRepository();
        this.guessRepository = new GuessRepository();
    }

    public static GameService getService() {
        if (service == null) {
            service = new GameService();
        }
        return service;
    }

    /**
     * Sign in method to get a user from store by username and password
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
     * @param username player username
     * @param password player password
     * @param name player name
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
        playerRepository.save(player);
        return player;
    }

    /**
     * method check validation user input
     * @param username player username
     * @param password player password
     * @param name player name
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
     * @param username username of player need create new game
     * @return new game instance
     */
    public GameSession createGame(String username) {
        GameSession gameSession = new GameSession(username);
        gameSession.setActive(true);

        // deactivate all other game
        deactivateAllGame(username);

        // save game to store
        gameSessionRepository.save(gameSession);
        return gameSession;
    }

    /**
     * Method deactivate all player's games by username
     * @param username username of player need deactivate games
     */
    private void deactivateAllGame(String username) {
        List<GameSession> gameSessions = gameSessionRepository.findByUsername(username);
        if (gameSessions == null) {
            return;
        }

        gameSessions.stream()
                .filter(GameSession::isActive)
                .toList()
                .forEach(game -> game.setActive(false));

    }

    /**
     * Method to get the player's active games by username
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

        activeGameSession.getGuesses().addAll(guesses);
        return activeGameSession;
    }

    /**
     * Get all finished games and sort it by game's guess and complete time
     * @return sorted game list
     */
    public List<GameSession> ranking() {
        // get all game from store
        List<GameSession> games = gameSessionRepository.findFinishedGames();

        // sort list by number of game's guess and time to complete game
       return games.stream()
               .sorted((game, other) -> new GameComparator().compare(game,other))
               .toList();
    }

    public List<GameSession> findAllGame() {
        return gameSessionRepository.findAll();
    }

    private static class GameComparator implements Comparator<GameSession>{

        @Override
        public int compare(GameSession o1, GameSession o2) {
            if (o1.getGuesses().size() > o2.getGuesses().size()){
                return 1;
            }else if (o1.getGuesses().size() < o2.getGuesses().size()){
                return -1;
            }else{
                return Long.compare(o1.getTime(), o2.getTime());
            }
        }
    }

    public Guess guessNumber(GameSession currentGame, int number) {
        // guess number and save guess to store

        int targetNumber = currentGame.getTargetNumber();
        Guess guess = new Guess(number,currentGame.getId());

        // Check user number with target number
        if (targetNumber == number){
            guess.setResult(GuessResult.CORRECT);
            currentGame.finished();
        }else if (targetNumber < number){
            guess.setResult(GuessResult.GREATER);
        }else {
            guess.setResult(GuessResult.LESSER);
        }

        // add guess to game's guess list
        currentGame.getGuesses().add(0,guess);
        return guess;
    }

    public List<Guess> findByGameId(String id) {
        return guessRepository.findByGameId(id);
    }

    public class GuessResult {
        private final static String CORRECT = "Chính xác";
        private final static String LESSER = "Số của bạn nhỏ hơn số ngẫu nhiên";
        private final static String GREATER = "Số của bạn lớn hơn số ngẫu nhiên";
    }
}
