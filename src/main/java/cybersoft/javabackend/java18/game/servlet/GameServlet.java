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
            default -> request.getRequestDispatcher(request.getContextPath() + UrlUtils.NOT_FOUND)
                    .forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getServletPath()) {
            case UrlUtils.GAME -> {
                processGame(req, resp);
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
            default -> req.getRequestDispatcher(req.getContextPath() + UrlUtils.NOT_FOUND)
                    .forward(req, resp);
        }
    }

    // Load current game or create new game
    private void loadGame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GameSession currentGame = getCurrentGameFromRequest(request);
        setCurrentGameToRequest(request, response, currentGame);
    }

    private void processGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get current player from request and player's active game in database
        GameSession currentGame = getCurrentGameFromRequest(req);

        String numberInput = req.getParameter("number");
        int number = Integer.parseInt(numberInput);

        // guess number with current game and player's input number
        currentGame.getGuesses().add(0, service.guessNumber(currentGame, number));

        setCurrentGameToRequest(req, resp, currentGame);
    }

    private void processRanking(HttpServletRequest request, HttpServletResponse response, int page) throws ServletException, IOException {
        // get sorted game list
        List<GameSession> sessions = service.rankingByPagination(page);
        request.setAttribute("totalPage", (int) Math.ceil(service.getSizeOfRank() / (JspUtils.DEFAULT_PAGE_SIZE * 1.0)));
        request.setAttribute("currentPage", page);

        request.setAttribute("games", sessions);
        request.getRequestDispatcher(JspUtils.RANK).forward(request, response);
    }

    private void processNewGame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get current game
        Player currentPlayer = (Player) request.getSession().getAttribute("currentUser");

        // create new game
        GameSession newGame = service.createGame(currentPlayer.getUsername());

        setCurrentGameToRequest(request, response, newGame);
    }

    private GameSession getCurrentGameFromRequest(HttpServletRequest request) {
        Player currentPlayer = (Player) request.getSession().getAttribute("currentUser");
        return service.getCurrentGame(currentPlayer.getUsername());
    }

    private void setCurrentGameToRequest(HttpServletRequest request, HttpServletResponse response,
                                         GameSession gameSession) throws ServletException, IOException {
        request.setAttribute("game", gameSession);
        request.getRequestDispatcher(JspUtils.GAME).forward(request, response);
    }

}
