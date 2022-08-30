package cybersoft.javabackend.java18.game.service.impl;

import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.repository.PlayerRepository;
import cybersoft.javabackend.java18.game.repository.TokenRepository;
import cybersoft.javabackend.java18.game.repository.impl.PlayerRepositoryImpl;
import cybersoft.javabackend.java18.game.repository.impl.TokenRepositoryImpl;
import cybersoft.javabackend.java18.game.service.AuthService;

public class AuthServiceImpl implements AuthService {
    private static AuthService authService = null;

    private final PlayerRepository playerRepository;
    private final TokenRepository tokenRepository;

    private AuthServiceImpl() {
        this.playerRepository = PlayerRepositoryImpl.getInstance();
        this.tokenRepository = TokenRepositoryImpl.getInstance();
    }

    public static AuthService getInstance() {
        if (authService == null) authService = new AuthServiceImpl();
        return authService;
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
    public Player findPlayerByUsername(String username) {
        return playerRepository.findByUsername(username);
    }
}
