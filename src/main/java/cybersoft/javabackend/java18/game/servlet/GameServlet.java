package cybersoft.javabackend.java18.game.servlet;

import cybersoft.javabackend.java18.game.model.GameSession;
import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.service.GameService;
import cybersoft.javabackend.java18.game.utils.JspUtils;
import cybersoft.javabackend.java18.game.utils.UrlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "gameServlet", urlPatterns = {UrlUtils.GAME, UrlUtils.NEW_GAME, UrlUtils.RANK})
public class GameServlet extends HttpServlet {

    private GameService service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = GameService.getService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case UrlUtils.GAME -> {
                loadGame(request, response);
            }
            case UrlUtils.RANK -> {
                request.setAttribute("currentPage", 1);
                processRanking(request, response, 1);
            }
            case UrlUtils.NEW_GAME -> {
                processNewGame(request, response);
            }
            default -> response.sendRedirect(JspUtils.NOT_FOUND);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getServletPath()) {
            case UrlUtils.GAME -> {
                loadGame(req, resp);
            }
            case UrlUtils.RANK -> {
                String currentPage = req.getParameter("currentPage");
                if (currentPage != null) {
                    int page = Integer.parseInt(currentPage);

                    if (req.getParameter("pagination").equalsIgnoreCase("previous")) {
                        processRanking(req, resp, page - 1);
                    } else {
                        processRanking(req, resp, page + 1);
                    }
                } else {
                    processRanking(req, resp, 1);
                }
            }
            default -> resp.sendRedirect(JspUtils.NOT_FOUND);
        }
    }

    // Load current game or create new game
    private void loadGame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Player currentPlayer = (Player) request.getSession().getAttribute("currentUser");
        GameSession currentGame = service.getCurrentGame(currentPlayer.getUsername());
        request.setAttribute("game", currentGame);
        request.getRequestDispatcher(JspUtils.GAME).forward(request, response);
    }

    private void processGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get current player from request and player's active game in store
        Player currentPlayer = (Player) req.getSession().getAttribute("currentUser");
        GameSession currentGame = service.getCurrentGame(currentPlayer.getUsername());
        String numberInput = req.getParameter("number");
        int number = Integer.parseInt(numberInput);

        // guess number with current game and player's input number
        service.guessNumber(currentGame, number);

        // put guess to request
        req.setAttribute("game", currentGame);
        req.getRequestDispatcher(JspUtils.GAME).forward(req, resp);
    }

    private void processRanking(HttpServletRequest request, HttpServletResponse response, int page) throws ServletException, IOException {
        // get sorted game list
        List<GameSession> gameSessions = service.rankingByPagination(page);
        request.setAttribute("totalPage", Math.round(service.getSizeOfRank() * 1.0 / JspUtils.DEFAULT_PAGE_SIZE));
        request.setAttribute("games", gameSessions);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher(JspUtils.RANK).forward(request, response);
    }

    private void processNewGame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get current game
        Player currentPlayer = (Player) request.getSession().getAttribute("currentUser");

        // create new game
        GameSession newGame = service.createGame(currentPlayer.getUsername());

        // set to request
        request.setAttribute("game", newGame);
        request.getRequestDispatcher(JspUtils.GAME).forward(request, response);
    }

}
