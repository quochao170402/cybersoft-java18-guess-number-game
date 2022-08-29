package cybersoft.javabackend.java18.game.servlet;

import cybersoft.javabackend.java18.game.model.GameSession;
import cybersoft.javabackend.java18.game.model.Guess;
import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.service.GameSessionService;
import cybersoft.javabackend.java18.game.service.GuessService;
import cybersoft.javabackend.java18.game.service.impl.GameSessionServiceImpl;
import cybersoft.javabackend.java18.game.service.impl.GuessServiceImpl;
import cybersoft.javabackend.java18.game.utils.JspUtils;
import cybersoft.javabackend.java18.game.utils.UrlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "accountServlet",
        urlPatterns = {
                UrlUtils.LIST_GAME,
                UrlUtils.VIEW_GAME,
                UrlUtils.CONTINUE_GAME,
                UrlUtils.DEACTIVATE_GAME,
                UrlUtils.ACTIVATE_GAME
        })
public class AccountServlet extends HttpServlet {
    private GameSessionService gameSessionService;
    private GuessService guessService;

    @Override
    public void init() throws ServletException {
        super.init();
        gameSessionService = GameSessionServiceImpl.getService();
        guessService = GuessServiceImpl.getService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        switch (request.getServletPath()) {
            case UrlUtils.LIST_GAME -> {
                request.setAttribute("currentPage", 1);
                processListGame(request, response, 1);
            }
            case UrlUtils.VIEW_GAME -> {
                request.setAttribute("currentPage", 1);
                processViewGame(request, response, 1);
            }
            case UrlUtils.CONTINUE_GAME -> {
                processContinueGame(request, response);
            }
            case UrlUtils.DEACTIVATE_GAME -> {
                setActiveGame(request, response, false);
            }
            case UrlUtils.ACTIVATE_GAME -> {
                setActiveGame(request, response, true);
            }
            default -> {
                request.getRequestDispatcher(request.getContextPath() + UrlUtils.NOT_FOUND)
                        .forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        switch (request.getServletPath()) {
            case UrlUtils.LIST_GAME -> {
                String currentPage = request.getParameter("currentPage");
                if (currentPage != null) {
                    int page = Integer.parseInt(currentPage);

                    if (request.getParameter("pagination").equalsIgnoreCase("previous")) {
                        processListGame(request, response, page - 1);
                    } else {
                        processListGame(request, response, page + 1);
                    }
                } else {
                    processListGame(request, response, 1);
                }
            }
            case UrlUtils.VIEW_GAME -> {
                String currentPage = request.getParameter("currentPage");
                if (currentPage != null) {
                    int page = Integer.parseInt(currentPage);

                    if (request.getParameter("pagination").equalsIgnoreCase("previous")) {
                        processViewGame(request, response, page - 1);
                    } else {
                        processViewGame(request, response, page + 1);
                    }
                } else {
                    processViewGame(request, response, 1);
                }
            }
            default -> {
                request.getRequestDispatcher(request.getContextPath() + UrlUtils.NOT_FOUND)
                        .forward(request, response);
            }
        }
    }

    private void processListGame(HttpServletRequest request, HttpServletResponse response, int page)
            throws ServletException, IOException {
        Player player = (Player) request.getSession().getAttribute("currentUser");
        List<GameSession> gameSessions = gameSessionService
                .findGamesByUsernameWithPagination(player.getUsername(), page);
        int totalPage = (int) Math.ceil(
                gameSessionService.countGamesByUsername(
                        player.getUsername())
                        /
                        (JspUtils.DEFAULT_PAGE_SIZE * 1.0));

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("games", gameSessions);

        request.getRequestDispatcher(JspUtils.LIST_GAME).forward(request, response);
    }

    private void processContinueGame(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("game-id");
        GameSession gameSession = gameSessionService.findGameById(id);
        gameSessionService.setGameActiveById(id, true);

        request.setAttribute("game", gameSession);

        request.getRequestDispatcher(JspUtils.GAME).forward(request, response);
    }

    private void processViewGame(HttpServletRequest request, HttpServletResponse response, int page) throws ServletException, IOException {
        String gameId = request.getParameter("game-id");
        List<Guess> guesses = guessService.findGuessesByGameIdWithPagination(gameId, page);

        int totalPage = (int) Math.ceil(
                guessService.countGuessesByGameId(gameId) / (JspUtils.DEFAULT_PAGE_SIZE * 1.0)
        );

        request.setAttribute("currentPage", page);
        request.setAttribute("id", gameId);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("guesses", guesses);

        request.getRequestDispatcher(JspUtils.VIEW_GAME).forward(request, response);
    }


    private void setActiveGame(HttpServletRequest request, HttpServletResponse response, boolean active)
            throws IOException {
        String gameId = request.getParameter("game-id");
        gameSessionService.setGameActiveById(gameId, active);

        response.sendRedirect(request.getContextPath() + UrlUtils.LIST_GAME);
    }
}
