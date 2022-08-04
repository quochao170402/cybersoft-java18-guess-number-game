package cybersoft.javabackend.java18.game.servlet;

import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.service.GameService;
import cybersoft.javabackend.java18.game.utils.JspUtils;
import cybersoft.javabackend.java18.game.utils.UrlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "authServlet", urlPatterns = {UrlUtils.SIGN_UP, UrlUtils.SIGN_IN, UrlUtils.LOG_OUT})
public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        switch (request.getServletPath()) {
            case UrlUtils.SIGN_UP -> {
                request.removeAttribute("errors");
                request.getRequestDispatcher(JspUtils.SIGN_UP)
                        .forward(request, response);
            }
            case UrlUtils.SIGN_IN -> {
                request.removeAttribute("errors");
                request.getRequestDispatcher(JspUtils.SIGN_IN)
                        .forward(request, response);
            }
            case UrlUtils.LOG_OUT -> {
                request.getSession().invalidate();
                request.getRequestDispatcher(JspUtils.SIGN_IN)
                        .forward(request, response);
            }
            default -> response.sendRedirect(request.getContextPath() + UrlUtils.NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        switch (request.getServletPath()) {
            case UrlUtils.SIGN_UP -> processSignUp(request, response);
            case UrlUtils.SIGN_IN -> processSignIn(request, response);
            default -> response.sendRedirect(request.getContextPath() + UrlUtils.NOT_FOUND);
        }
    }

    private void processSignIn(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Player player = GameService.getService().signIn(username, password);
        if (player != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", player);
            request.removeAttribute("errors");
            response.sendRedirect(request.getContextPath() + UrlUtils.GAME);
        } else {
            request.setAttribute("errors", "Thông tin người chơi không hợp lệ hoặc đã được sử dụng");
            request.getRequestDispatcher(JspUtils.SIGN_IN).forward(request, response);
        }

    }

    private void processSignUp(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        Player player = GameService.getService().signUp(username, password, name);

        if (player != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", player);
            request.removeAttribute("errors");
            response.sendRedirect(request.getContextPath() + UrlUtils.GAME);
        } else {
            request.setAttribute("errors", "Thông tin người chơi không hợp lệ hoặc đã được sử dụng");
            request.getRequestDispatcher(JspUtils.SIGN_UP).forward(request, response);
        }
    }

}
