package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.security.Token;

public interface TokenRepository {
    Token getToken(String selector);

    boolean resetToken(String selector, String validator);

    boolean insert(Token token);

    void deleteToken(String selector);
}
