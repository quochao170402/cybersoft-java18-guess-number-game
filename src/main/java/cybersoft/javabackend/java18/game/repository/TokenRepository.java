package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.model.Token;

public interface TokenRepository {
    Token getToken(String selector);

    void resetToken(String selector, String validator);

    void insert(Token token);

    void deleteToken(String selector);
}
