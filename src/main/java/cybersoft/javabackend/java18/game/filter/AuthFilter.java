package cybersoft.javabackend.java18.game.filter;

import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.model.Token;
import cybersoft.javabackend.java18.game.repository.TokenRepository;
import cybersoft.javabackend.java18.game.repository.impl.PlayerRepositoryImpl;
import cybersoft.javabackend.java18.game.repository.impl.TokenRepositoryImpl;
import cybersoft.javabackend.java18.game.utils.UrlUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebFilter(urlPatterns = {UrlUtils.ALL})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (isAuthUrl(req) || isLoginUser(req) || isRememberUser(req, resp)) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + UrlUtils.LOGIN);
        }

        /*
         process before the request get in servlet
         chain.doFilter(request,response)
         process response from servlet
        */
    }

    private boolean isRememberUser(HttpServletRequest req, HttpServletResponse resp) {
        Token token = getTokenFromRequest(req);
        if (token == null) {
            return false;
        }

        Player player = PlayerRepositoryImpl.getRepository().findByUsername(token.getUsername());
        req.getSession().setAttribute("currentUser", player);

        addCookies(resp, token.getSelector(), token.getValidator());

        resetToken(token.getSelector());

        return true;
    }

    private void resetToken(String selector) {
        TokenRepository tokenRepository = TokenRepositoryImpl.getRepository();
        String validator = UUID.randomUUID().toString();
        tokenRepository.resetToken(selector, validator);
    }

    private Token getTokenFromRequest(HttpServletRequest req) {
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

        TokenRepository tokenRepository = TokenRepositoryImpl.getRepository();
        Token token = tokenRepository.getToken(selector);

        if (token == null) return null;

        if (validator.equals(token.getValidator())) {
            return token;
        }

        return null;
    }

    private void addCookies(HttpServletResponse resp, String selector, String validator) {
        Cookie cookieSelector = new Cookie("selector", selector);
        cookieSelector.setMaxAge(60 * 60 * 24 * 30);
        Cookie cookieValidator = new Cookie("validator", validator);
        cookieValidator.setMaxAge(60 * 60 * 24 * 30);

        resp.addCookie(cookieSelector);
        resp.addCookie(cookieValidator);
    }


    private boolean isLoginUser(HttpServletRequest req) {
        return req.getSession().getAttribute("currentUser") != null;
    }

    private boolean isAuthUrl(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith(UrlUtils.REGISTER)
                || path.startsWith(UrlUtils.LOGIN);
    }
}
