package cybersoft.javabackend.java18.game.security;

import cybersoft.javabackend.java18.game.service.AuthService;
import cybersoft.javabackend.java18.game.service.impl.AuthServiceImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class TokenHelper {
    private static TokenHelper instance = null;
    private final AuthService authService;


    private TokenHelper() {
        authService = AuthServiceImpl.getService();
    }

    public static TokenHelper getInstance() {
        if (instance == null) instance = new TokenHelper();
        return instance;
    }

    public Token getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        String selector = "";
        String validator = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("selector")) {
                selector = cookie.getValue();
            } else if (cookie.getName().equals("validator")) {
                validator = cookie.getValue();
            }
        }

        if (selector == null || validator == null) {
            return null;
        }

        Token token = authService.getToken(selector);

        if (token == null) return null;

        if (!validator.equals(token.getValidator())) {
            return null;
        }

        return token;
    }

    public void addTokenToCookies(HttpServletResponse response, Token token) {
        Cookie cookieSelector = new Cookie("selector", token.getSelector());
        cookieSelector.setMaxAge(60 * 60 * 24 * 30);
        Cookie cookieValidator = new Cookie("validator", token.getValidator());
        cookieValidator.setMaxAge(60 * 60 * 24 * 30);

        response.addCookie(cookieSelector);
        response.addCookie(cookieValidator);
    }

    public Token createToken(String username) {
        String selector = UUID.randomUUID().toString();
        String validator = UUID.randomUUID().toString();
        Token newToken = new Token(selector, validator, username);
        if (authService.saveToken(newToken)) {
            return newToken;
        }
        return null;
    }

    public Token resetToken(String selector) {
        String validator = UUID.randomUUID().toString();
        return authService.resetToken(selector, validator);
    }
}
