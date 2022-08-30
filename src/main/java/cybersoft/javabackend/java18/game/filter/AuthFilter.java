package cybersoft.javabackend.java18.game.filter;

import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.security.Token;
import cybersoft.javabackend.java18.game.security.TokenHelper;
import cybersoft.javabackend.java18.game.service.AuthService;
import cybersoft.javabackend.java18.game.service.impl.AuthServiceImpl;
import cybersoft.javabackend.java18.game.utils.UrlUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {UrlUtils.ALL})
public class AuthFilter implements Filter {
    private TokenHelper tokenHelper;
    private AuthService authService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        tokenHelper = TokenHelper.getInstance();
        authService = AuthServiceImpl.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
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
        Token token = tokenHelper.getTokenFromRequest(req);
        if (token == null) {
            return false;
        }

        Player player = authService.findPlayerByUsername(token.getUsername());
        req.getSession().setAttribute("currentUser", player);

        token = tokenHelper.resetToken(token.getSelector());
        tokenHelper.addTokenToCookies(resp, token);
        return true;
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
