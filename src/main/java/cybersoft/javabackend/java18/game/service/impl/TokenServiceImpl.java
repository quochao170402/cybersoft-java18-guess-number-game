package cybersoft.javabackend.java18.game.service.impl;

import cybersoft.javabackend.java18.game.repository.TokenRepository;
import cybersoft.javabackend.java18.game.repository.impl.TokenRepositoryImpl;
import cybersoft.javabackend.java18.game.security.Token;
import cybersoft.javabackend.java18.game.service.TokenService;

public class TokenServiceImpl implements TokenService {
    private static TokenService tokenService = null;

    private final TokenRepository tokenRepository;

    private TokenServiceImpl() {
        this.tokenRepository = TokenRepositoryImpl.getInstance();
    }

    public static TokenService getInstance() {
        if (tokenService == null) tokenService = new TokenServiceImpl();
        return tokenService;
    }

    @Override
    public boolean saveToken(Token token) {
        return tokenRepository.insert(token);
    }

    @Override
    public boolean deleteToken(String selector) {
        return tokenRepository.deleteToken(selector);
    }

    @Override
    public Token getToken(String selector) {
        return tokenRepository.getToken(selector);
    }

    @Override
    public Token resetToken(String selector, String validator) {
        if (tokenRepository.resetToken(selector, validator)) {
            return getToken(selector);
        }
        return null;
    }
}
