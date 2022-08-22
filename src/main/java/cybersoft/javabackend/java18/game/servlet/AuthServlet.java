package cybersoft.javabackend.java18.game.servlet;

import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.model.Token;
import cybersoft.javabackend.java18.game.service.GameService;
import cybersoft.javabackend.java18.game.service.impl.GameServiceImpl;
import cybersoft.javabackend.java18.game.utils.JspUtils;
import cybersoft.javabackend.java18.game.utils.UrlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "authServlet", urlPatterns = {UrlUtils.REGISTER, UrlUtils.LOGIN, UrlUtils.LOG_OUT})
public class AuthServlet extends HttpServlet {

    private GameService gameService = null;

    @Override
    public void init() throws ServletException {
        super.init();
        gameService = GameServiceImpl.getService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        switch (request.getServletPath()) {
            case UrlUtils.REGISTER -> {
                request.removeAttribute("errors");
                request.getRequestDispatcher(JspUtils.REGISTER)
                        .forward(request, response);
            }
            case UrlUtils.LOGIN -> {
                request.removeAttribute("errors");
                request.getRequestDispatcher(JspUtils.LOGIN)
                        .forward(request, response);
            }
            case UrlUtils.LOG_OUT -> {
                processLogout(request);
                request.getRequestDispatcher(JspUtils.LOGIN)
                        .forward(request, response);
            }
            default -> request.getRequestDispatcher(request.getContextPath() + UrlUtils.NOT_FOUND)
                    .forward(request, response);
        }
    }

    private void processLogout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String selector = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("selector")) {
                selector = cookie.getValue();
                break;
            }
        }

        if (!"".equals(selector)) {
            gameService.deleteToken(selector);

            Cookie cookieSelector = new Cookie("selector", "");
            cookieSelector.setMaxAge(0);
            Cookie cookieValidator = new Cookie("selector", "");
            cookieValidator.setMaxAge(0);
        }

        request.getSession().invalidate();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        switch (request.getServletPath()) {
            case UrlUtils.REGISTER -> processRegister(request, response);
            case UrlUtils.LOGIN -> processLogin(request, response);
            default -> request.getRequestDispatcher(request.getContextPath() + UrlUtils.NOT_FOUND)
                    .forward(request, response);
        }
    }

    private void processLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Player player = gameService.login(username, password);
        if (player != null) {
            if (request.getParameter("remember") != null) {
                processRememberUser(player, request, response);
            }
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", player);
            request.removeAttribute("errors");
            response.sendRedirect(request.getContextPath() + UrlUtils.GAME);
        } else {
            request.setAttribute("errors", "Thông tin người chơi không hợp lệ hoặc đã được sử dụng");
            request.getRequestDispatcher(JspUtils.LOGIN).forward(request, response);
        }

    }

    private void processRememberUser(Player player, HttpServletRequest request, HttpServletResponse response) {
        Token token = getTokenFromRequest(request);
        if (token != null) {
            if (token.getUsername().equals(player.getUsername())) {
                resetToken(token.getSelector());
            }
            addCookies(response, token);
        } else {
            String selector = UUID.randomUUID().toString();
            String validator = UUID.randomUUID().toString();
            Token newToken = new Token(selector, validator, player.getUsername());
            gameService.saveToken(newToken);
            addCookies(response, newToken);
        }
    }

    private void resetToken(String selector) {
        String validator = UUID.randomUUID().toString();
        gameService.resetToken(selector, validator);
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

        Token token = gameService.getToken(selector);

        if (token == null) return null;

        if (validator.equals(token.getValidator())) {
            return token;
        }

        return null;
    }

    private void addCookies(HttpServletResponse response, Token token) {
        Cookie cookieSelector = new Cookie("selector", token.getSelector());
        cookieSelector.setMaxAge(60 * 60 * 24 * 30);
        Cookie cookieValidator = new Cookie("validator", token.getValidator());
        cookieValidator.setMaxAge(60 * 60 * 24 * 30);

        response.addCookie(cookieSelector);
        response.addCookie(cookieValidator);
    }

    private void processRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        Player player = gameService.register(username, password, name);

        if (player != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", player);
            request.removeAttribute("errors");
            response.sendRedirect(request.getContextPath() + UrlUtils.GAME);
        } else {
            request.setAttribute("errors", "Thông tin người chơi không hợp lệ hoặc đã được sử dụng");
            request.getRequestDispatcher(JspUtils.REGISTER).forward(request, response);
        }
    }

}
