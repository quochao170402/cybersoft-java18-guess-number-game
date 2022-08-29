package cybersoft.javabackend.java18.game.servlet;

import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.security.Token;
import cybersoft.javabackend.java18.game.security.TokenHelper;
import cybersoft.javabackend.java18.game.service.AuthService;
import cybersoft.javabackend.java18.game.service.impl.AuthServiceImpl;
import cybersoft.javabackend.java18.game.utils.JspUtils;
import cybersoft.javabackend.java18.game.utils.UrlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "authServlet", urlPatterns = {UrlUtils.REGISTER, UrlUtils.LOGIN, UrlUtils.LOG_OUT})
public class AuthServlet extends HttpServlet {

    private AuthService authService = null;

    @Override
    public void init() throws ServletException {
        super.init();
        authService = AuthServiceImpl.getService();
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
            authService.deleteToken(selector);

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
        Player player = authService.login(username, password);
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
        Token token = TokenHelper.getInstance().getTokenFromRequest(request);

        if (token == null) {
            token = TokenHelper.getInstance().createToken(player.getUsername());
        } else {
            if (token.getUsername().equals(player.getUsername())) {
                token = TokenHelper.getInstance().resetToken(token.getSelector());
            }
        }
        if (token == null) return;
        TokenHelper.getInstance().addTokenToCookies(response, token);

    }

    private void processRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        Player player = authService.register(username, password, name);

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
