package cybersoft.javabackend.java18.game.service;

import cybersoft.javabackend.java18.game.security.Token;

public interface TokenService {

    /**
     * Delete token by selector
     *
     * @param selector token's selector
     * @return true if delete successful or else false
     */
    boolean deleteToken(String selector);

    /**
     * Save a token to database
     *
     * @param token token
     * @return true if save successful or else false
     */
    boolean saveToken(Token token);

    /**
     * Get an existed token from database by selector
     *
     * @param selector token's selector
     * @return a existed token if found or else null
     */
    Token getToken(String selector);

    /**
     * Reset token validator
     *
     * @param selector  token's selector
     * @param validator new validator value
     * @return a token after reset validator
     */
    Token resetToken(String selector, String validator);
}

