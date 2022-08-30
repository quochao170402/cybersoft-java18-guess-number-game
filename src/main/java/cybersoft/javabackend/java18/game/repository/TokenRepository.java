package cybersoft.javabackend.java18.game.repository;

import cybersoft.javabackend.java18.game.security.Token;

public interface TokenRepository {

    /**
     * Get existed token from database
     *
     * @param selector token's selector saved in cookies
     * @return a token if found token in database or return null value
     */
    Token getToken(String selector);

    /**
     * Reset validator value by selector
     *
     * @param selector  token's selector
     * @param validator token's validator
     * @return true if reset successful or else false
     */
    boolean resetToken(String selector, String validator);

    /**
     * Insert a token to database
     *
     * @param token user's token
     * @return true if insert successful or else false
     */
    boolean insert(Token token);

    /**
     * Delete a token by selector
     *
     * @param selector token's selector
     */
    boolean deleteToken(String selector);
}
